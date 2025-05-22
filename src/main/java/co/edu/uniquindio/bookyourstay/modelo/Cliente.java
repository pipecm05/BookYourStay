package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCuenta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Cliente extends Usuario {
    private Billetera billetera;
    private List<Reserva> reservas;
    private List<Reseña> reseñas;
    private TipoCuenta tipoCuenta;
    private LocalDate fechaRegistro;
    private LocalDate fechaNacimiento;
    private boolean cuentaActiva;

    public Cliente() {
        super();
        this.billetera = new Billetera();
        this.reservas = new ArrayList<>();
        this.reseñas = new ArrayList<>();
        this.tipoCuenta = TipoCuenta.REGULAR;
        this.fechaRegistro = LocalDate.now();
        this.cuentaActiva = true;
    }

    // Métodos de Billetera
    public void recargarBilletera(float monto, String metodoPago) throws Exception {
        validarCuentaActiva();
        billetera.recargarSaldo(monto, metodoPago);
    }

    public boolean tieneSaldoSuficiente(float monto) {
        return billetera.getSaldo() >= monto;
    }

    public boolean pagarReserva(float monto, String concepto)throws Exception {
        validarCuentaActiva();
        return billetera.descontarSaldo(monto, concepto);
    }

    public float consultarSaldo() {
        return billetera.getSaldo();
    }

    public List<Transaccion> obtenerHistorialTransacciones() {
        return billetera.obtenerHistorial();
    }

    public List<Transaccion> filtrarTransaccionesPorTipo(String tipo) {
        return billetera.filtrarTransacciones(tipo);
    }

    // Métodos de Reservas
    public Reserva crearReserva(Alojamiento alojamiento, LocalDate fechaInicio,
                                LocalDate fechaFin, int numHuespedes) throws Exception {
        validarCuentaActiva();

        if (!alojamiento.estaDisponible(fechaInicio, fechaFin)) {
            throw new Exception("El alojamiento no está disponible para las fechas seleccionadas");
        }

        if (numHuespedes > alojamiento.getCapacidadMax()) {
            throw new Exception("Número de huéspedes excede la capacidad del alojamiento");
        }

        float costoTotal = alojamiento.calcularCostoTotal(
                (int) fechaInicio.until(fechaFin).getDays()
        );

        if (!tieneSaldoSuficiente(costoTotal)) {
            throw new Exception("Saldo insuficiente en la billetera");
        }

        Reserva reserva = Reserva.builder()
                .id(UUID.randomUUID().toString())
                .alojamiento(alojamiento)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .numHuespedes(numHuespedes)
                .estado(EstadoReserva.CONFIRMADA)
                .total(costoTotal)
                .build();

        if (pagarReserva(costoTotal, "Reserva " + reserva.getId())) {
            agregarReserva(reserva);
            alojamiento.agregarReserva(reserva);
            return reserva;
        }

        throw new Exception("No se pudo procesar el pago de la reserva");
    }

    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public boolean cancelarReserva(String reservaId) throws Exception {
        validarCuentaActiva();
        Reserva reserva = buscarReserva(reservaId);

        if (reserva == null) {
            throw new Exception("Reserva no encontrada");
        }

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new Exception("Solo se pueden cancelar reservas confirmadas");
        }

        if (reserva.getFechaInicio().minusDays(2).isBefore(LocalDate.now())) {
            throw new Exception("Solo se pueden cancelar con más de 48 horas de anticipación");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        billetera.recargarSaldo(reserva.getTotal() * 0.8f, "Reembolso cancelación");
        return true;
    }

    public List<Reserva> obtenerReservasActivas() {
        return reservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA)
                .collect(Collectors.toList());
    }

    public List<Reserva> obtenerReservasPorEstado(EstadoReserva estado) {
        return reservas.stream()
                .filter(r -> r.getEstado() == estado)
                .collect(Collectors.toList());
    }

    public List<Reserva> obtenerHistorialReservas() {
        return new ArrayList<>(reservas);
    }

    // Métodos de Reseñas
    public Reseña crearReseña(Alojamiento alojamiento, Reserva reserva,
                              int calificacion, String comentario) throws Exception {
        validarCuentaActiva();

        if (!reserva.getEstado().equals(EstadoReserva.COMPLETADA)) {
            throw new Exception("Solo puede dejar reseña en reservas completadas");
        }

        if (calificacion < 1 || calificacion > 5) {
            throw new Exception("La calificación debe estar entre 1 y 5 estrellas");
        }

        if (comentario == null || comentario.isBlank()) {
            throw new Exception("El comentario no puede estar vacío");
        }

        if (reseñas.stream().anyMatch(r -> r.getReserva().equals(reserva))) {
            throw new Exception("Ya existe una reseña para esta reserva");
        }

        Reseña reseña = Reseña.builder()
                .id(UUID.randomUUID().toString())
                .cliente(this)
                .alojamiento(alojamiento)
                .reserva(reserva)
                .calificacion(calificacion)
                .comentario(comentario.trim())
                .fecha(LocalDateTime.now())
                .verificada(false)
                .build();

        reseñas.add(reseña);
        alojamiento.agregarReseña(reseña);
        actualizarTipoUsuario();
        return reseña;
    }

    public List<Reseña> obtenerMisReseñas() {
        return new ArrayList<>(reseñas);
    }

    // Métodos de gestión de cuenta
    public void desactivarCuenta() {
        this.cuentaActiva = false;
    }

    public void reactivarCuenta() {
        this.cuentaActiva = true;
    }

    // Métodos privados de ayuda
    private void validarCuentaActiva() throws Exception {
        if (!cuentaActiva) {
            throw new Exception("La cuenta del cliente está inactiva");
        }
    }

    private Reserva buscarReserva(String reservaId) {
        return reservas.stream()
                .filter(r -> r.getId().equals(reservaId))
                .findFirst()
                .orElse(null);
    }

    private void actualizarTipoUsuario() {
        long reseñasPositivas = reseñas.stream()
                .filter(r -> r.getCalificacion() >= 4)
                .count();

        if (reseñasPositivas >= 5) {
            this.tipoCuenta = TipoCuenta.PREMIUM;
        }
    }

    // Builder para Cliente
    public static class ClienteBuilder extends UsuarioBuilder<ClienteBuilder> {
        private float saldoInicial;
        private List<Reserva> reservasIniciales;
        private List<Reseña> reseñasIniciales;

        public ClienteBuilder() {
            super();
            this.reservasIniciales = new ArrayList<>();
            this.reseñasIniciales = new ArrayList<>();
        }

        public ClienteBuilder saldoInicial(float saldoInicial) {
            this.saldoInicial = saldoInicial;
            return this;
        }

        public ClienteBuilder agregarReservaInicial(Reserva reserva) {
            this.reservasIniciales.add(reserva);
            return this;
        }

        public ClienteBuilder agregarReseñaInicial(Reseña reseña) {
            this.reseñasIniciales.add(reseña);
            return this;
        }

        @Override
        protected ClienteBuilder self() {
            return this;
        }

        @Override
        public Cliente build() {
            Cliente cliente = new Cliente();
            cliente.setId(id);
            cliente.setNombre(nombre);
            cliente.setEmail(email);
            cliente.setContraseña(contraseña);
            cliente.setTelefono(telefono);
            cliente.setCedula(cedula);
            cliente.setDireccion(direccion);
            cliente.setFotoPerfilUrl(fotoPerfilUrl);

            if (this.saldoInicial > 0) {
                try {
                    cliente.recargarBilletera(this.saldoInicial, "Depósito inicial");
                } catch (Exception e) {
                    throw new RuntimeException("Error al recargar billetera durante la creación del cliente", e);
                }
            }

            this.reservasIniciales.forEach(cliente::agregarReserva);
            this.reseñasIniciales.forEach(cliente.getReseñas()::add);

            return cliente;
        }
    }

    public static ClienteBuilder builder() {
        return new ClienteBuilder();
    }

    @Override
    public String toString() {
        return String.format("Cliente: %s (%s) - %s - Registro: %s",
                getNombre(),
                getEmail(),
                tipoCuenta,
                fechaRegistro);
    }
}