package pe.edu.utp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos", uniqueConstraints = @UniqueConstraint(name = "uk_producto_codigo", columnNames = "codigo"))
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio.")
    @Size(max = 40, message = "El código no puede superar los 40 caracteres.")
    @Column(nullable = false, unique = true, length = 40)
    private String codigo;

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotNull(message = "Selecciona una categoría.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false, foreignKey = @ForeignKey(name = "fk_producto_categoria"))
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", foreignKey = @ForeignKey(name = "fk_producto_proveedor"))
    private Proveedor proveedor;

    @NotNull(message = "El precio de compra es obligatorio.")
    @DecimalMin(value = "0.00", message = "El precio de compra no puede ser negativo.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal compra = BigDecimal.ZERO;

    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor que cero.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal venta = BigDecimal.ONE;

    @NotNull(message = "El stock es obligatorio.")
    @PositiveOrZero(message = "El stock no puede ser negativo.")
    @Column(nullable = false)
    private Integer stock = 0;

    @NotNull(message = "El stock mínimo es obligatorio.")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo.")
    @Column(nullable = false)
    private Integer stockMinimo = 0;

    @Size(max = 500, message = "La URL de imagen es demasiado larga.")
    @Column(length = 500)
    private String imagenUrl;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<DetalleVenta> detallesVenta = new ArrayList<>();

    public Producto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public BigDecimal getCompra() { return compra; }
    public void setCompra(BigDecimal compra) { this.compra = compra; }
    public BigDecimal getVenta() { return venta; }
    public void setVenta(BigDecimal venta) { this.venta = venta; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public List<DetalleVenta> getDetallesVenta() { return detallesVenta; }
    public BigDecimal getValorInventario() {
        if (compra == null || stock == null) return BigDecimal.ZERO;
        return compra.multiply(BigDecimal.valueOf(stock));
    }
}
