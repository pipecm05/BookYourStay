package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoTransaccion;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {
    private String id;
    private String tipo; // RECARGA, PAGO, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECIBIDA, REEMBOLSO
    private float monto;
    private String descripcion;
    private LocalDateTime fecha;
    private String cuentaRelacionada;
    private String metodoPago;
    private String referencia;
    private EstadoTransaccion estado;
    private String codigoAutorizacion;
    private String dispositivoOrigen;

    public Transaccion(String tipo, float monto, String descripcion, String cuentaRelacionada) {
        this.id = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.cuentaRelacionada = cuentaRelacionada;
        this.estado = EstadoTransaccion.COMPLETADA;
        this.referencia = generarReferencia();
    }

    /**
     * Valida los datos básicos de la transacción
     */
    public void validar() {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de transacción es requerido");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es requerida");
        }
    }

    /**
     * Marca la transacción como fallida
     */
    public void marcarComoFallida(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Debe proporcionar un motivo");
        }
        this.estado = EstadoTransaccion.FALLIDA;
        this.descripcion += " | Fallida: " + motivo;
    }

    /**
     * Reversa una transacción
     */
    public Transaccion generarTransaccionReverso(String motivo) {
        if (!estado.equals(EstadoTransaccion.COMPLETADA)) {
            throw new IllegalStateException("Solo se pueden reversar transacciones completadas");
        }

        return Transaccion.builder()
                .id("TXN-" + UUID.randomUUID().toString().substring(0, 8))
                .tipo("REVERSO")
                .monto(-this.monto)
                .descripcion("Reverso de " + this.id + ": " + motivo)
                .fecha(LocalDateTime.now())
                .cuentaRelacionada(this.cuentaRelacionada)
                .metodoPago(this.metodoPago)
                .referencia(this.referencia + "-REV")
                .estado(EstadoTransaccion.COMPLETADA)
                .codigoAutorizacion(generarCodigoAutorizacion())
                .build();
    }

    /**
     * Genera un reporte detallado de la transacción
     */
    public String generarReporte() {
        return String.format(
                "Transacción #%s\n" +
                        "Tipo: %s\n" +
                        "Fecha: %s\n" +
                        "Monto: $%,.2f\n" +
                        "Estado: %s\n" +
                        "Cuenta: %s\n" +
                        "Método pago: %s\n" +
                        "Referencia: %s\n" +
                        "Código autorización: %s\n" +
                        "Descripción: %s",
                id,
                tipo,
                fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                monto,
                estado,
                cuentaRelacionada,
                metodoPago != null ? metodoPago : "N/A",
                referencia != null ? referencia : "N/A",
                codigoAutorizacion != null ? codigoAutorizacion : "N/A",
                descripcion
        );
    }

    /**
     * Verifica si es una transacción de ingreso
     */
    public boolean esIngreso() {
        return tipo.equals("RECARGA") || tipo.equals("TRANSFERENCIA_RECIBIDA") || tipo.equals("REEMBOLSO");
    }

    /**
     * Verifica si es una transacción de egreso
     */
    public boolean esEgreso() {
        return tipo.equals("PAGO") || tipo.equals("TRANSFERENCIA_ENVIADA");
    }

    // Métodos privados de ayuda
    private String generarReferencia() {
        return "REF-" + fecha.getDayOfMonth() +
                fecha.getMonthValue() +
                fecha.getYear() % 100;
    }

    private String generarCodigoAutorizacion() {
        return "AUTH-" + (int)(Math.random() * 1000000);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s $%,.2f - %s",
                fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                tipo,
                estado.getIcono(),
                monto,
                descripcion.length() > 20 ? descripcion.substring(0, 20) + "..." : descripcion);
    }
}
