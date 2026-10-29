package pe.edu.utp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProveedorDTO {
    private Long id;
    @NotBlank(message = "El nombre del proveedor es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    private String nombre;
    @NotBlank(message = "El RUC es obligatorio.")
    @Pattern(regexp = "[0-9]{11}", message = "El RUC debe contener exactamente 11 dígitos.")
    private String ruc;
    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "[0-9+()\\s-]{7,15}", message = "Ingresa un teléfono válido (7 a 15 caracteres).")
    private String telefono;
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres.")
    private String direccion;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
