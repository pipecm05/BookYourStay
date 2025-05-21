package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoTransaccion;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Billetera {
    private float saldo;
    private String numeroCuenta;
    private List<Transaccion> historialTransacciones;
    private boolean activa;

    public Billetera() {
        this.saldo = 0;
        this.numeroCuenta = generarNumeroCuenta();
        this.historialTransacciones = new ArrayList<>();
        this.activa = true;
    }

    /**
     * Recarga saldo en la billetera
     * @param monto Cantidad a recargar (debe ser positivo)
     * @param metodoPago Método de pago utilizado
     * @throws IllegalArgumentException Si el monto no es válido o la billetera está inactiva
     */
    public void recargarSaldo(float monto, String metodoPago) {
        validarBilleteraActiva();
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        this.saldo += monto;
        registrarTransaccion("RECARGA", monto, "Recarga mediante " + metodoPago);
    }

    /**
     * Descuenta saldo de la billetera
     * @param monto Cantidad a descontar (debe ser positivo)
     * @param concepto Descripción de la transacción
     * @return true si el descuento fue exitoso, false si no hay saldo suficiente
     * @throws IllegalArgumentException Si el monto no es válido
     */
    public boolean descontarSaldo(float monto, String concepto) {
        validarBilleteraActiva();
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (saldo >= monto) {
            this.saldo -= monto;
            registrarTransaccion("PAGO", -monto, concepto);
            return true;
        }
        return false;
    }

    /**
     * Transfiere saldo a otra billetera
     * @param destino Billetera receptora
     * @param monto Cantidad a transferir
     * @param concepto Descripción de la transferencia
     * @throws IllegalArgumentException Si la transferencia no es válida
     * @throws IllegalStateException Si no hay saldo suficiente o billeteras inactivas
     */
    public void transferir(Billetera destino, float monto, String concepto) {
        validarBilleteraActiva();
        destino.validarBilleteraActiva();

        if (destino.equals(this)) {
            throw new IllegalArgumentException("No puede transferir a la misma billetera");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (saldo < monto) {
            throw new IllegalStateException("Saldo insuficiente para la transferencia");
        }

        this.saldo -= monto;
        destino.saldo += monto;

        String referencia = generarReferencia();
        registrarTransaccion("TRANSFERENCIA_ENVIADA", -monto, concepto + " | Ref: " + referencia);
        destino.registrarTransaccion("TRANSFERENCIA_RECIBIDA", monto, concepto + " | Ref: " + referencia);
    }

    /**
     * Obtiene el historial de transacciones filtrado por tipo
     * @param tipo Tipo de transacción a filtrar (RECARGA, PAGO, TRANSFERENCIA)
     * @return Lista de transacciones filtradas
     */
    public List<Transaccion> filtrarTransacciones(String tipo) {
        return historialTransacciones.stream()
                .filter(t -> t.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el saldo disponible
     * @return Saldo actual
     */
    public float consultarSaldo() {
        return saldo;
    }

    /**
     * Obtiene una copia del historial de transacciones
     * @return Lista inmutable de transacciones
     */
    public List<Transaccion> obtenerHistorial() {
        return new ArrayList<>(historialTransacciones);
    }

    /**
     * Activa/desactiva la billetera
     * @param activa Estado deseado
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
        registrarTransaccion("SISTEMA", 0, "Billetera " + (activa ? "activada" : "desactivada"));
    }

    // Métodos privados de ayuda

    private void registrarTransaccion(String tipo, float monto, String descripcion) {
        Transaccion transaccion = Transaccion.builder()
                .id(UUID.randomUUID().toString())
                .tipo(tipo)
                .monto(monto)
                .descripcion(descripcion)
                .fecha(LocalDateTime.now())
                .cuentaRelacionada(this.numeroCuenta)
                .estado(EstadoTransaccion.COMPLETADA) // Opcional
                .build();

        historialTransacciones.add(transaccion);
    }

    private void validarBilleteraActiva() {
        if (!activa) {
            throw new IllegalStateException("La billetera está inactiva");
        }
    }

    private String generarNumeroCuenta() {
        return "BWT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generarReferencia() {
        return "REF-" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return String.format("Billetera [Cuenta: %s, Saldo: $%,.2f, Estado: %s]",
                numeroCuenta,
                saldo,
                activa ? "Activa" : "Inactiva");
    }
}