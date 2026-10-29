package pe.edu.utp.controller;
import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
import pe.edu.utp.service.ProductoService;
@Controller @RequestMapping("/inventario")
public class InventarioController {
 private final ProductoService service; public InventarioController(ProductoService service){this.service=service;}
 @GetMapping public String inventario(Model model){model.addAttribute("productos",service.listarTodos());return "pages/inventario";}
 @GetMapping("/lista") public String legacy(){return "redirect:/inventario";}
}
