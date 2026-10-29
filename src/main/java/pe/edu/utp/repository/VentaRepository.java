package pe.edu.utp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    boolean existsByCliente_Id(Long clienteId);

    @EntityGraph(attributePaths = {"cliente", "detalles", "detalles.producto"})
    @Query("select distinct v from Venta v order by v.fecha desc")
    List<Venta> listarConRelaciones();

    @EntityGraph(attributePaths = {"cliente", "detalles", "detalles.producto"})
    @Query("select distinct v from Venta v where v.id = :id")
    Optional<Venta> buscarConRelaciones(@Param("id") Long id);
}
