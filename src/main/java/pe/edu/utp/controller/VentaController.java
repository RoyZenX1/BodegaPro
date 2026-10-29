package pe.edu.utp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.dto.VentaDTO;
import pe.edu.utp.model.Venta;
import pe.edu.utp.service.ClienteService;
import pe.edu.utp.service.ProductoService;
import pe.edu.utp.service.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    public VentaController(VentaService v, ClienteService c, ProductoService p) { ventaService = v; clienteService = c; productoService = p; }

    @GetMapping public String lista(Model model) { cargarModelo(model); model.addAttribute("ventaForm", new VentaDTO()); return "pages/ventas"; }
    @GetMapping("/lista") public String legacy() { return "redirect:/ventas"; }
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String termino, Model model) {
        cargarModelo(model); model.addAttribute("ventas", ventaService.buscarPorTermino(termino)); model.addAttribute("termino", termino); model.addAttribute("ventaForm", new VentaDTO()); return "pages/ventas";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Venta venta = ventaService.buscar(id).orElse(null);
        if (venta == null || venta.getDetalles().isEmpty()) { ra.addFlashAttribute("error", "La venta solicitada no existe o no tiene detalles."); return "redirect:/ventas"; }
        VentaDTO dto = new VentaDTO(); dto.setId(venta.getId()); dto.setClienteId(venta.getCliente().getId());
        dto.setProductoId(venta.getDetalles().get(0).getProducto().getId()); dto.setCantidad(venta.getDetalles().get(0).getCantidad());
        cargarModelo(model); model.addAttribute("ventaForm", dto); return "pages/ventas";
    }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("ventaForm") VentaDTO formulario, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) { cargarModelo(model); model.addAttribute("mostrarErrores", true); return "pages/ventas"; }
        try {
            ventaService.registrar(formulario);
            ra.addFlashAttribute("mensaje", formulario.getId() == null ? "Venta registrada exitosamente." : "Venta actualizada exitosamente.");
            return "redirect:/ventas";
        } catch (IllegalArgumentException ex) {
            cargarModelo(model); model.addAttribute("ventaForm", formulario); model.addAttribute("error", ex.getMessage()); model.addAttribute("mostrarErrores", true); return "pages/ventas";
        }
    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try { ventaService.eliminar(id); ra.addFlashAttribute("mensaje", "Venta eliminada y stock restituido."); }
        catch (IllegalArgumentException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/ventas";
    }
    private void cargarModelo(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos()); model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("productos", productoService.listarTodos()); model.addAttribute("totalVentas", ventaService.totalVendido());
    }
}
