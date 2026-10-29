package pe.edu.utp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByDocumento(String documento);
    boolean existsByDocumentoAndIdNot(String documento, Long id);
    List<Cliente> findByNombreContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
            String nombre, String documento);
}
