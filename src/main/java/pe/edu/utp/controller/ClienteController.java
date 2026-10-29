package pe.edu.utp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.dto.ClienteDTO;
import pe.edu.utp.model.Cliente;
import pe.edu.utp.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }
    @GetMapping public String lista(Model model) { cargarLista(model); model.addAttribute("cliente", new ClienteDTO()); return "pages/clientes"; }
    @GetMapping("/lista") public String legacy() { return "redirect:/clientes"; }
    @GetMapping("/nuevo") public String nuevo(Model model) { cargarLista(model); model.addAttribute("cliente", new ClienteDTO()); model.addAttribute("mostrarModal", true); return "pages/clientes"; }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Cliente c = service.buscar(id).orElse(null);
        if (c == null) { ra.addFlashAttribute("error", "El cliente solicitado no existe."); return "redirect:/clientes"; }
        cargarLista(model); model.addAttribute("cliente", convertir(c)); model.addAttribute("mostrarModal", true); return "pages/clientes";
    }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") ClienteDTO formulario, BindingResult result, Model model, RedirectAttributes ra) {
        if (service.existeOtro(formulario.getDocumento(), formulario.getId())) result.rejectValue("documento", "cliente.duplicado", "Ya existe un cliente con ese documento.");
        Cliente existente = formulario.getId() == null ? null : service.buscar(formulario.getId()).orElse(null);
        if (formulario.getId() != null && existente == null) result.reject("cliente.noExiste", "El cliente que intentas editar ya no existe.");
        if (result.hasErrors()) { cargarLista(model); model.addAttribute("mostrarModal", true); return "pages/clientes"; }
        Cliente entidad = existente == null ? new Cliente() : existente;
        entidad.setNombre(formulario.getNombre()); entidad.setDocumento(formulario.getDocumento());
        entidad.setTelefono(formulario.getTelefono()); entidad.setDireccion(formulario.getDireccion());
        service.guardar(entidad); ra.addFlashAttribute("mensaje", "Cliente guardado exitosamente."); return "redirect:/clientes";
    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try { service.eliminar(id); ra.addFlashAttribute("mensaje", "Cliente eliminado."); }
        catch (IllegalStateException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/clientes";
    }
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String termino, Model model) {
        model.addAttribute("clientes", service.buscar(termino)); model.addAttribute("cliente", new ClienteDTO()); model.addAttribute("termino", termino); return "pages/clientes";
    }
    private void cargarLista(Model model) { model.addAttribute("clientes", service.listarTodos()); }
    private ClienteDTO convertir(Cliente c) {
        ClienteDTO dto = new ClienteDTO(); dto.setId(c.getId()); dto.setNombre(c.getNombre()); dto.setDocumento(c.getDocumento()); dto.setTelefono(c.getTelefono()); dto.setDireccion(c.getDireccion()); return dto;
    }
}
