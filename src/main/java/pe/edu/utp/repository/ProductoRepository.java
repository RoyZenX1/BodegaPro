package pe.edu.utp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
    boolean existsByCategoria_Id(Long categoriaId);
    boolean existsByProveedor_Id(Long proveedorId);

    @Override
    @EntityGraph(attributePaths = {"categoria", "proveedor"})
    List<Producto> findAll();

    @EntityGraph(attributePaths = {"categoria", "proveedor"})
    Optional<Producto> findWithRelationsById(Long id);

    @EntityGraph(attributePaths = {"categoria", "proveedor"})
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCaseOrCategoria_NombreContainingIgnoreCase(
            String nombre, String codigo, String categoriaNombre);
}
