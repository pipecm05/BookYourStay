package co.edu.uniquindio.bookyourstay.modelo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Huesped {
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String genero;
    private String nacionalidad;
    private String contactoEmergencia;
    private String telefonoEmergencia;
    private String necesidadesEspeciales;

    @Override
    public String toString() {
        return String.format("%s (%s %s)", nombreCompleto, tipoDocumento, numeroDocumento);
    }
}