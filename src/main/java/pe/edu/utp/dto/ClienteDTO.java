package pe.edu.utp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteDTO {
    private Long id;
    @NotBlank(message = "El nombre del cliente es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    private String nombre;
    @NotBlank(message = "El documento es obligatorio.")
    @Pattern(regexp = "(?:[0-9]{8}|[0-9]{11})", message = "Ingresa un DNI de 8 dígitos o un RUC de 11 dígitos.")
    private String documento;
    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "[0-9+()\\s-]{7,15}", message = "Ingresa un teléfono válido (7 a 15 caracteres).")
    private String telefono;
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres.")
    private String direccion;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
