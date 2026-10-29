package pe.edu.utp.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.model.Proveedor;
import pe.edu.utp.repository.ProductoRepository;
import pe.edu.utp.repository.ProveedorRepository;

@Service
public class ProveedorService {
    private final ProveedorRepository repository;
    private final ProductoRepository productoRepository;
    public ProveedorService(ProveedorRepository repository, ProductoRepository productoRepository) {
        this.repository = repository; this.productoRepository = productoRepository;
    }
    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscar(Long id) { return repository.findById(id); }
    @Transactional(readOnly = true)
    public boolean existe(String ruc) { return ruc != null && repository.existsByRuc(ruc.trim()); }
    @Transactional(readOnly = true)
    public boolean existeOtro(String ruc, Long id) {
        if (ruc == null) return false;
        return id == null ? existe(ruc) : repository.existsByRucAndIdNot(ruc.trim(), id);
    }
    @Transactional
    public Proveedor guardar(Proveedor proveedor) {
        proveedor.setNombre(proveedor.getNombre().trim());
        proveedor.setRuc(proveedor.getRuc().trim());
        proveedor.setTelefono(proveedor.getTelefono().trim());
        if (proveedor.getDireccion() != null) proveedor.setDireccion(proveedor.getDireccion().trim());
        return repository.save(proveedor);
    }
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) return;
        if (productoRepository.existsByProveedor_Id(id)) {
            throw new IllegalStateException("No se puede eliminar el proveedor porque tiene productos asociados.");
        }
        repository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<Proveedor> buscar(String termino) {
        if (termino == null || termino.isBlank()) return repository.findAll();
        String t = termino.trim();
        return repository.findByNombreContainingIgnoreCaseOrRucContainingIgnoreCase(t, t);
    }
}
