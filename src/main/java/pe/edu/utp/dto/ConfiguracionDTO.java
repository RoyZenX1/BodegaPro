package pe.edu.utp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ConfiguracionDTO {
    @NotBlank(message = "El nombre de la bodega es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres.")
    private String nombreBodega;
    @NotBlank(message = "Selecciona una moneda.")
    @Pattern(regexp = "PEN|USD", message = "La moneda debe ser PEN o USD.")
    private String moneda;
    @NotBlank(message = "Selecciona un tema.")
    @Pattern(regexp = "claro|oscuro", message = "El tema debe ser claro u oscuro.")
    private String tema;
    public String getNombreBodega() { return nombreBodega; }
    public void setNombreBodega(String nombreBodega) { this.nombreBodega = nombreBodega; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
}
