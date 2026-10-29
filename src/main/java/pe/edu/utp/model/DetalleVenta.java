package pe.edu.utp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La venta asociada es obligatoria.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_detalle_venta_venta"))
    private Venta venta;

    @NotNull(message = "El producto es obligatorio.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_detalle_venta_producto"))
    private Producto producto;

    @NotNull(message = "La cantidad es obligatoria.")
    @Positive(message = "La cantidad debe ser mayor que cero.")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio.")
    @PositiveOrZero(message = "El precio unitario no puede ser negativo.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    public DetalleVenta() {}
    public DetalleVenta(Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        this.producto = producto; this.cantidad = cantidad; this.precioUnitario = precioUnitario;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getSubtotal() {
        if (cantidad == null || precioUnitario == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
