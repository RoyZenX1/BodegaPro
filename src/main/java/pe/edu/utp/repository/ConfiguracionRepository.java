package pe.edu.utp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.model.Configuracion;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    Optional<Configuracion> findFirstByOrderByIdAsc();
}
