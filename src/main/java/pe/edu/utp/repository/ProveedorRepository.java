package pe.edu.utp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByRuc(String ruc);
    boolean existsByRucAndIdNot(String ruc, Long id);
    List<Proveedor> findByNombreContainingIgnoreCaseOrRucContainingIgnoreCase(String nombre, String ruc);
}
