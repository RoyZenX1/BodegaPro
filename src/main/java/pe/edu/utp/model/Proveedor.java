package pe.edu.utp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proveedores", uniqueConstraints = @UniqueConstraint(name = "uk_proveedor_ruc", columnNames = "ruc"))
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proveedor es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El RUC es obligatorio.")
    @Pattern(regexp = "[0-9]{11}", message = "El RUC debe contener exactamente 11 dígitos.")
    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "[0-9+()\\s-]{7,15}", message = "Ingresa un teléfono válido (7 a 15 caracteres).")
    @Column(nullable = false, length = 20)
    private String telefono;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres.")
    @Column(length = 255)
    private String direccion;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();

    public Proveedor() {}
    public Proveedor(Long id, String nombre, String ruc, String telefono, String direccion) {
        this.id = id; this.nombre = nombre; this.ruc = ruc; this.telefono = telefono; this.direccion = direccion;
    }
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
    public List<Producto> getProductos() { return productos; }
}
