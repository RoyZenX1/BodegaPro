package pe.edu.utp.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.model.Categoria;
import pe.edu.utp.repository.CategoriaRepository;
import pe.edu.utp.repository.ProductoRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;
    private final ProductoRepository productoRepository;
    public CategoriaService(CategoriaRepository repository, ProductoRepository productoRepository) {
        this.repository = repository; this.productoRepository = productoRepository;
    }
    @Transactional(readOnly = true)
    public List<Categoria> listarTodos() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Optional<Categoria> buscar(Long id) { return repository.findById(id); }
    @Transactional(readOnly = true)
    public boolean existe(String nombre) { return nombre != null && repository.existsByNombreIgnoreCase(nombre.trim()); }
    @Transactional(readOnly = true)
    public boolean existeOtro(String nombre, Long id) {
        if (nombre == null) return false;
        return id == null ? existe(nombre) : repository.existsByNombreIgnoreCaseAndIdNot(nombre.trim(), id);
    }
    @Transactional
    public Categoria guardar(Categoria categoria) {
        categoria.setNombre(categoria.getNombre().trim());
        if (categoria.getDescripcion() != null) categoria.setDescripcion(categoria.getDescripcion().trim());
        return repository.save(categoria);
    }
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) return;
        if (productoRepository.existsByCategoria_Id(id)) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados.");
        }
        repository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<Categoria> buscar(String termino) {
        if (termino == null || termino.isBlank()) return repository.findAll();
        String t = termino.trim();
        return repository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(t, t);
    }
}
