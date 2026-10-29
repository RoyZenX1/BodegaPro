package pe.edu.utp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.service.*;
@Controller
public class DashboardController {
 private final ProductoService productoService;
 private final ClienteService clienteService;
 private final VentaService ventaService;
 public DashboardController(ProductoService p,ClienteService c,VentaService v){this.productoService=p;this.clienteService=c;this.ventaService=v;}
 @GetMapping("/dashboard")
 public String dashboard(Model model){
   model.addAttribute("productosTotal",productoService.contar());
   model.addAttribute("clientesTotal",clienteService.listarTodos().size());
   model.addAttribute("stockBajo",productoService.stockBajo());
   model.addAttribute("productosBajo",productoService.stockBajoList());
   model.addAttribute("ventasTotal",ventaService.totalVendido());
   model.addAttribute("ventas",ventaService.listarTodos());
   return "pages/dashboard";
 }
}
