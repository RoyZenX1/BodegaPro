package pe.edu.utp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "configuracion")
public class Configuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la bodega es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nombreBodega = "BodegaPro";

    @NotBlank(message = "Selecciona una moneda.")
    @Pattern(regexp = "PEN|USD", message = "La moneda debe ser PEN o USD.")
    @Column(nullable = false, length = 3)
    private String moneda = "PEN";

    @NotBlank(message = "Selecciona un tema.")
    @Pattern(regexp = "claro|oscuro", message = "El tema debe ser claro u oscuro.")
    @Column(nullable = false, length = 10)
    private String tema = "claro";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreBodega() { return nombreBodega; }
    public void setNombreBodega(String nombreBodega) { this.nombreBodega = nombreBodega; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
}
