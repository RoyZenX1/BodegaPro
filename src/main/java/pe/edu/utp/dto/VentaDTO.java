package pe.edu.utp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VentaDTO {
    private Long id;
    @NotNull(message = "Selecciona un cliente.")
    private Long clienteId;
    @NotNull(message = "Selecciona un producto.")
    private Long productoId;
    @NotNull(message = "La cantidad es obligatoria.")
    @Positive(message = "La cantidad debe ser como mínimo 1.")
    private Integer cantidad = 1;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
