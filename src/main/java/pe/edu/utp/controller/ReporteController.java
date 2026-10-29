package pe.edu.utp.controller;
import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
import pe.edu.utp.service.*;
@Controller @RequestMapping("/reportes")
public class ReporteController {
 private final ProductoService productoService; private final VentaService ventaService; private final ClienteService clienteService;
 public ReporteController(ProductoService p,VentaService v,ClienteService c){productoService=p;ventaService=v;clienteService=c;}
 @GetMapping public String reportes(Model model){model.addAttribute("productos",productoService.listarTodos());model.addAttribute("ventas",ventaService.listarTodos());model.addAttribute("clientes",clienteService.listarTodos());model.addAttribute("totalVentas",ventaService.totalVendido());return "pages/reportes";}
}
