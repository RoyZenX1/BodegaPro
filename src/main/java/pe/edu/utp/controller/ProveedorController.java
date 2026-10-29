package pe.edu.utp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.dto.ProveedorDTO;
import pe.edu.utp.model.Proveedor;
import pe.edu.utp.service.ProveedorService;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {
    private final ProveedorService service;
    public ProveedorController(ProveedorService service) { this.service = service; }
    @GetMapping public String lista(Model model) { cargarLista(model); model.addAttribute("proveedor", new ProveedorDTO()); return "pages/proveedores"; }
    @GetMapping("/lista") public String legacy() { return "redirect:/proveedores"; }
    @GetMapping("/nuevo") public String nuevo(Model model) { cargarLista(model); model.addAttribute("proveedor", new ProveedorDTO()); model.addAttribute("mostrarModal", true); return "pages/proveedores"; }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Proveedor p = service.buscar(id).orElse(null);
        if (p == null) { ra.addFlashAttribute("error", "El proveedor solicitado no existe."); return "redirect:/proveedores"; }
        cargarLista(model); model.addAttribute("proveedor", convertir(p)); model.addAttribute("mostrarModal", true); return "pages/proveedores";
    }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proveedor") ProveedorDTO formulario, BindingResult result, Model model, RedirectAttributes ra) {
        if (service.existeOtro(formulario.getRuc(), formulario.getId())) result.rejectValue("ruc", "proveedor.duplicado", "Ya existe un proveedor con ese RUC.");
        Proveedor existente = formulario.getId() == null ? null : service.buscar(formulario.getId()).orElse(null);
        if (formulario.getId() != null && existente == null) result.reject("proveedor.noExiste", "El proveedor que intentas editar ya no existe.");
        if (result.hasErrors()) { cargarLista(model); model.addAttribute("mostrarModal", true); return "pages/proveedores"; }
        Proveedor entidad = existente == null ? new Proveedor() : existente;
        entidad.setNombre(formulario.getNombre()); entidad.setRuc(formulario.getRuc()); entidad.setTelefono(formulario.getTelefono()); entidad.setDireccion(formulario.getDireccion());
        service.guardar(entidad); ra.addFlashAttribute("mensaje", "Proveedor guardado exitosamente."); return "redirect:/proveedores";
    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try { service.eliminar(id); ra.addFlashAttribute("mensaje", "Proveedor eliminado."); }
        catch (IllegalStateException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/proveedores";
    }
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String termino, Model model) {
        model.addAttribute("proveedores", service.buscar(termino)); model.addAttribute("proveedor", new ProveedorDTO()); model.addAttribute("termino", termino); return "pages/proveedores";
    }
    private void cargarLista(Model model) { model.addAttribute("proveedores", service.listarTodos()); }
    private ProveedorDTO convertir(Proveedor p) {
        ProveedorDTO dto = new ProveedorDTO(); dto.setId(p.getId()); dto.setNombre(p.getNombre()); dto.setRuc(p.getRuc()); dto.setTelefono(p.getTelefono()); dto.setDireccion(p.getDireccion()); return dto;
    }
}
