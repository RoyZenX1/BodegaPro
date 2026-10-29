package pe.edu.utp.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.model.Producto;
import pe.edu.utp.repository.DetalleVentaRepository;
import pe.edu.utp.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final DetalleVentaRepository detalleVentaRepository;
    public ProductoService(ProductoRepository repository, DetalleVentaRepository detalleVentaRepository) {
        this.repository = repository; this.detalleVentaRepository = detalleVentaRepository;
    }
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Optional<Producto> buscar(Long id) { return repository.findWithRelationsById(id); }
    @Transactional(readOnly = true)
    public boolean existe(String codigo) { return codigo != null && repository.existsByCodigoIgnoreCase(codigo.trim()); }
    @Transactional(readOnly = true)
    public boolean existeOtro(String codigo, Long id) {
        if (codigo == null) return false;
        return id == null ? existe(codigo) : repository.existsByCodigoIgnoreCaseAndIdNot(codigo.trim(), id);
    }
    @Transactional
    public Producto guardar(Producto producto) {
        producto.setCodigo(producto.getCodigo().trim());
        producto.setNombre(producto.getNombre().trim());
        if (producto.getImagenUrl() != null && producto.getImagenUrl().isBlank()) producto.setImagenUrl(null);
        return repository.save(producto);
    }
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) return;
        if (detalleVentaRepository.existsByProducto_Id(id)) {
            throw new IllegalStateException("No se puede eliminar el producto porque aparece en ventas registradas.");
        }
        repository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public long contar() { return repository.count(); }
    @Transactional(readOnly = true)
    public long stockBajo() { return repository.findAll().stream().filter(p -> p.getStock() <= p.getStockMinimo()).count(); }
    @Transactional(readOnly = true)
    public List<Producto> buscar(String termino) {
        if (termino == null || termino.isBlank()) return repository.findAll();
        String t = termino.trim();
        return repository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCaseOrCategoria_NombreContainingIgnoreCase(t, t, t);
    }
    @Transactional(readOnly = true)
    public List<Producto> stockBajoList() {
        return repository.findAll().stream().filter(p -> p.getStock() <= p.getStockMinimo()).toList();
    }
}
