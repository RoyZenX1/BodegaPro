package pe.edu.utp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El cliente es obligatorio.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_venta_cliente"))
    private Cliente cliente;

    @NotNull(message = "La fecha de la venta es obligatoria.")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}
    public Venta(Long id, Cliente cliente, LocalDateTime fecha) {
        this.id = id; this.cliente = cliente; this.fecha = fecha;
    }
    @PrePersist
    public void antesDeGuardar() { if (fecha == null) fecha = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles.clear();
        if (detalles != null) {
            for (DetalleVenta detalle : detalles) { detalle.setVenta(this); this.detalles.add(detalle); }
        }
    }
    public BigDecimal getTotal() {
        return detalles.stream().map(DetalleVenta::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
