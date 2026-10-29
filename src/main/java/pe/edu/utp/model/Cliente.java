package pe.edu.utp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes", uniqueConstraints = @UniqueConstraint(name = "uk_cliente_documento", columnNames = "documento"))
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El documento es obligatorio.")
    @Pattern(regexp = "(?:[0-9]{8}|[0-9]{11})", message = "Ingresa un DNI de 8 dígitos o un RUC de 11 dígitos.")
    @Column(nullable = false, unique = true, length = 11)
    private String documento;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "[0-9+()\\s-]{7,15}", message = "Ingresa un teléfono válido (7 a 15 caracteres).")
    @Column(nullable = false, length = 20)
    private String telefono;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres.")
    @Column(length = 255)
    private String direccion;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Venta> ventas = new ArrayList<>();

    public Cliente() {}
    public Cliente(Long id, String nombre, String documento, String telefono, String direccion) {
        this.id = id; this.nombre = nombre; this.documento = documento; this.telefono = telefono; this.direccion = direccion;
    }
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
    public List<Venta> getVentas() { return ventas; }
}
