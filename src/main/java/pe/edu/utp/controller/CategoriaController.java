package pe.edu.utp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.dto.CategoriaDTO;
import pe.edu.utp.model.Categoria;
import pe.edu.utp.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("categorias", service.listarTodos());
        model.addAttribute("categoria", new CategoriaDTO());
        return "pages/categorias";
    }
    @GetMapping("/lista") public String legacy() { return "redirect:/categorias"; }
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categorias", service.listarTodos());
        model.addAttribute("categoria", new CategoriaDTO());
        model.addAttribute("mostrarModal", true);
        return "pages/categorias";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Categoria categoria = service.buscar(id).orElse(null);
        if (categoria == null) { ra.addFlashAttribute("error", "La categoría solicitada no existe."); return "redirect:/categorias"; }
        model.addAttribute("categorias", service.listarTodos());
        model.addAttribute("categoria", convertir(categoria));
        model.addAttribute("mostrarModal", true);
        return "pages/categorias";
    }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") CategoriaDTO formulario, BindingResult result,
                          Model model, RedirectAttributes ra) {
        if (service.existeOtro(formulario.getNombre(), formulario.getId())) {
            result.rejectValue("nombre", "categoria.duplicada", "Ya existe una categoría con ese nombre.");
        }
        Categoria existente = formulario.getId() == null ? null : service.buscar(formulario.getId()).orElse(null);
        if (formulario.getId() != null && existente == null) result.reject("categoria.noExiste", "La categoría que intentas editar ya no existe.");
        if (result.hasErrors()) {
            model.addAttribute("categorias", service.listarTodos());
            model.addAttribute("mostrarModal", true);
            return "pages/categorias";
        }
        Categoria entidad = existente == null ? new Categoria() : existente;
        entidad.setNombre(formulario.getNombre());
        entidad.setDescripcion(formulario.getDescripcion());
        service.guardar(entidad);
        ra.addFlashAttribute("mensaje", "Categoría guardada exitosamente.");
        return "redirect:/categorias";
    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try { service.eliminar(id); ra.addFlashAttribute("mensaje", "Categoría eliminada."); }
        catch (IllegalStateException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/categorias";
    }
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String termino, Model model) {
        model.addAttribute("categorias", service.buscar(termino));
        model.addAttribute("categoria", new CategoriaDTO());
        model.addAttribute("termino", termino);
        return "pages/categorias";
    }
    private CategoriaDTO convertir(Categoria c) {
        CategoriaDTO dto = new CategoriaDTO(); dto.setId(c.getId()); dto.setNombre(c.getNombre()); dto.setDescripcion(c.getDescripcion()); return dto;
    }
}
