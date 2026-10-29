package pe.edu.utp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductoDTO {
    private Long id;
    @NotBlank(message = "El código es obligatorio.")
    @Size(max = 40, message = "El código no puede superar los 40 caracteres.")
    private String codigo;
    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    private String nombre;
    @NotNull(message = "Selecciona una categoría.")
    private Long categoriaId;
    private Long proveedorId;
    @NotNull(message = "El precio de compra es obligatorio.")
    @DecimalMin(value = "0.00", message = "El precio de compra no puede ser negativo.")
    private BigDecimal compra = BigDecimal.ZERO;
    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor que cero.")
    private BigDecimal venta = BigDecimal.ONE;
    @NotNull(message = "El stock es obligatorio.")
    @PositiveOrZero(message = "El stock no puede ser negativo.")
    private Integer stock = 0;
    @NotNull(message = "El stock mínimo es obligatorio.")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo.")
    private Integer stockMinimo = 0;
    @Size(max = 500, message = "La URL de imagen es demasiado larga.")
    @Pattern(regexp = "^(https?://\\S+)?$", message = "La URL debe empezar con http:// o https://.")
    private String imagenUrl;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
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
}
