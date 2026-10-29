package pe.edu.utp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.dto.ProductoDTO;
import pe.edu.utp.model.Categoria;
import pe.edu.utp.model.Producto;
import pe.edu.utp.model.Proveedor;
import pe.edu.utp.service.CategoriaService;
import pe.edu.utp.service.ProductoService;
import pe.edu.utp.service.ProveedorService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    public ProductoController(ProductoService p, CategoriaService c, ProveedorService pr) {
        this.productoService = p; this.categoriaService = c; this.proveedorService = pr;
    }
    @GetMapping public String lista(Model model) { cargarCatalogos(model); model.addAttribute("productos", productoService.listarTodos()); model.addAttribute("producto", new ProductoDTO()); return "pages/productos"; }
    @GetMapping("/lista") public String listaLegacy() { return "redirect:/productos"; }
    @GetMapping("/nuevo") public String nuevo(Model model) { cargarCatalogos(model); model.addAttribute("productos", productoService.listarTodos()); model.addAttribute("producto", new ProductoDTO()); model.addAttribute("mostrarModal", true); return "pages/productos"; }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Producto p = productoService.buscar(id).orElse(null);
        if (p == null) { ra.addFlashAttribute("error", "El producto solicitado no existe."); return "redirect:/productos"; }
        cargarCatalogos(model); model.addAttribute("productos", productoService.listarTodos()); model.addAttribute("producto", convertir(p)); model.addAttribute("mostrarModal", true); return "pages/productos";
    }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") ProductoDTO formulario, BindingResult result, Model model, RedirectAttributes ra) {
        if (productoService.existeOtro(formulario.getCodigo(), formulario.getId())) result.rejectValue("codigo", "producto.duplicado", "Ya existe un producto con ese código.");
        Categoria categoria = formulario.getCategoriaId() == null ? null : categoriaService.buscar(formulario.getCategoriaId()).orElse(null);
        if (formulario.getCategoriaId() != null && categoria == null) result.rejectValue("categoriaId", "producto.categoriaNoExiste", "La categoría seleccionada no existe.");
        Proveedor proveedor = formulario.getProveedorId() == null ? null : proveedorService.buscar(formulario.getProveedorId()).orElse(null);
        if (formulario.getProveedorId() != null && proveedor == null) result.rejectValue("proveedorId", "producto.proveedorNoExiste", "El proveedor seleccionado no existe.");
        Producto existente = formulario.getId() == null ? null : productoService.buscar(formulario.getId()).orElse(null);
        if (formulario.getId() != null && existente == null) result.reject("producto.noExiste", "El producto que intentas editar ya no existe.");
        if (result.hasErrors()) { cargarCatalogos(model); model.addAttribute("productos", productoService.listarTodos()); model.addAttribute("mostrarModal", true); return "pages/productos"; }
        Producto entidad = existente == null ? new Producto() : existente;
        entidad.setCodigo(formulario.getCodigo()); entidad.setNombre(formulario.getNombre()); entidad.setCategoria(categoria); entidad.setProveedor(proveedor);
        entidad.setCompra(formulario.getCompra()); entidad.setVenta(formulario.getVenta()); entidad.setStock(formulario.getStock()); entidad.setStockMinimo(formulario.getStockMinimo()); entidad.setImagenUrl(formulario.getImagenUrl());
        productoService.guardar(entidad); ra.addFlashAttribute("mensaje", "Producto guardado exitosamente."); return "redirect:/productos";
    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try { productoService.eliminar(id); ra.addFlashAttribute("mensaje", "Producto eliminado."); }
        catch (IllegalStateException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/productos";
    }
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String termino, Model model) {
        cargarCatalogos(model); model.addAttribute("productos", productoService.buscar(termino)); model.addAttribute("producto", new ProductoDTO()); model.addAttribute("termino", termino); return "pages/productos";
    }
    private void cargarCatalogos(Model model) { model.addAttribute("categorias", categoriaService.listarTodos()); model.addAttribute("proveedores", proveedorService.listarTodos()); }
    private ProductoDTO convertir(Producto p) {
        ProductoDTO dto = new ProductoDTO(); dto.setId(p.getId()); dto.setCodigo(p.getCodigo()); dto.setNombre(p.getNombre());
        dto.setCategoriaId(p.getCategoria() == null ? null : p.getCategoria().getId()); dto.setProveedorId(p.getProveedor() == null ? null : p.getProveedor().getId());
        dto.setCompra(p.getCompra()); dto.setVenta(p.getVenta()); dto.setStock(p.getStock()); dto.setStockMinimo(p.getStockMinimo()); dto.setImagenUrl(p.getImagenUrl()); return dto;
    }
}
