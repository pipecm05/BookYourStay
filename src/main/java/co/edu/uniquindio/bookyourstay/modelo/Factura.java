package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCuenta;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {
    private String id;
    private LocalDateTime fecha;
    private Reserva reserva;
    private Cliente cliente;
    private float subtotal;
    private float descuentos;
    private float impuestos;
    private float total;
    private String metodoPago;
    private String estado; // PAGADA, PENDIENTE, CANCELADA
    private String codigoQR;

    /**
     * Genera una factura completa a partir de una reserva
     */
    public static Factura generarFactura(Reserva reserva, String metodoPago) {
        float subtotal = reserva.getAlojamiento().calcularCostoBase(reserva.getNumeroNoches());
        float descuentos = calcularDescuentos(reserva);
        float impuestos = calcularImpuestos(subtotal - descuentos);

        Factura factura = Factura.builder()
                .id("FAC-" + UUID.randomUUID().toString().substring(0, 8))
                .fecha(LocalDateTime.now())
                .reserva(reserva)
                .cliente(reserva.getCliente())
                .subtotal(subtotal)
                .descuentos(descuentos)
                .impuestos(impuestos)
                .total(subtotal - descuentos + impuestos)
                .metodoPago(metodoPago)
                .estado("PAGADA")
                .build();

        factura.generarCodigoQR();
        return factura;
    }

    /**
     * Calcula descuentos aplicables a la reserva
     */
    private static float calcularDescuentos(Reserva reserva) {
        float descuento = 0;

        // Descuento por temporada baja (ejemplo)
        if (esTemporadaBaja(reserva.getFechaInicio())) {
            descuento += reserva.getSubtotal() * 0.1f; // 10% de descuento
        }

        // Descuento por cliente premium (ejemplo)
        if (reserva.getCliente().getTipoCuenta() == TipoCuenta.PREMIUM) {
            descuento += reserva.getSubtotal() * 0.05f; // 5% adicional
        }

        return descuento;
    }

    /**
     * Calcula impuestos aplicables
     */
    private static float calcularImpuestos(float baseImponible) {
        // IVA colombiano (19%)
        return baseImponible * 0.19f;
    }

    /**
     * Genera un código QR simulado para la factura
     */
    private void generarCodigoQR() {
        // En una implementación real se generaría un código QR real
        this.codigoQR = "QR_" + this.id + "_" + this.total;
    }

    /**
     * Verifica si es temporada baja (ejemplo simplificado)
     */
    private static boolean esTemporadaBaja(LocalDate fecha) {
        // Lógica de ejemplo - temporada baja de mayo a junio y septiembre a octubre
        int month = fecha.getMonthValue();
        return (month >= 5 && month <= 6) || (month >= 9 && month <= 10);
    }

    /**
     * Obtiene la representación detallada de la factura
     */
    public String obtenerDetalle() {
        return String.format(
                "FACTURA #%s\n" +
                        "Fecha: %s\n" +
                        "Cliente: %s (%s)\n" +
                        "Alojamiento: %s\n" +
                        "Periodo: %s a %s (%d noches)\n\n" +
                        "Subtotal: $%,.2f\n" +
                        "Descuentos: -$%,.2f\n" +
                        "Impuestos (19%%): $%,.2f\n" +
                        "TOTAL: $%,.2f\n\n" +
                        "Método de pago: %s\n" +
                        "Estado: %s\n" +
                        "Código QR: %s",
                id,
                fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                cliente.getNombre(),
                cliente.getCedula(),
                reserva.getAlojamiento().getNombre(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getNumeroNoches(),
                subtotal,
                descuentos,
                impuestos,
                total,
                metodoPago,
                estado,
                codigoQR
        );
    }

    /**
     * Cancela la factura y aplica reembolsos si es necesario
     */
    public void cancelar() throws Exception {
        if ("CANCELADA".equals(estado)) {
            throw new IllegalStateException("La factura ya está cancelada");
        }

        this.estado = "CANCELADA";

        if ("PAGADA".equals(estado)) {
            float reembolso = total * 0.8f;
            cliente.recargarBilletera(reembolso, "Reembolso factura " + id);
        }
    }

    @Override
    public String toString() {
        return String.format("Factura #%s - %s - $%,.2f", id, estado, total);
    }
}
