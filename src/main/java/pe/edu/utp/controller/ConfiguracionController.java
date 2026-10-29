package pe.edu.utp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.dto.ConfiguracionDTO;
import pe.edu.utp.model.Configuracion;
import pe.edu.utp.service.ConfiguracionService;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {
    private final ConfiguracionService service;
    public ConfiguracionController(ConfiguracionService service) { this.service = service; }
    @GetMapping public String configuracion(Model model) { model.addAttribute("configuracion", convertir(service.obtener())); return "pages/configuracion"; }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("configuracion") ConfiguracionDTO dto, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) return "pages/configuracion";
        Configuracion nueva = new Configuracion(); nueva.setNombreBodega(dto.getNombreBodega()); nueva.setMoneda(dto.getMoneda()); nueva.setTema(dto.getTema());
        service.guardar(nueva); ra.addFlashAttribute("mensaje", "Configuración actualizada."); return "redirect:/configuracion";
    }
    private ConfiguracionDTO convertir(Configuracion c) {
        ConfiguracionDTO dto = new ConfiguracionDTO(); dto.setNombreBodega(c.getNombreBodega()); dto.setMoneda(c.getMoneda()); dto.setTema(c.getTema()); return dto;
    }
}
