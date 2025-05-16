package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.repositorio.ReservaRepositorio;
import co.edu.uniquindio.bookyourstay.repositorio.ReseñaRepositorio;
import co.edu.uniquindio.bookyourstay.utilidades.EnviadorEmail;
import co.edu.uniquindio.bookyourstay.utilidades.GeneradorQR;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservaServicio implements IReservaServicio {
    private static ReservaServicio instancia;
    private final ReservaRepositorio reservaRepositorio;
    private final ReseñaRepositorio reseñaRepositorio;
    private final GeneradorQR generadorQR;

    private ReservaServicio() {
        this.reservaRepositorio = ReservaRepositorio.getInstancia();
        this.reseñaRepositorio = ReseñaRepositorio.getInstancia();
        this.generadorQR = new GeneradorQR();
    }

    public static ReservaServicio getInstancia() {
        if (instancia == null) {
            instancia = new ReservaServicio();
        }
        return instancia;
    }

    @Override
    public Reserva crearReserva(Cliente cliente, Alojamiento alojamiento,
                                LocalDate fechaInicio, LocalDate fechaFin,
                                int numHuespedes) throws Exception {
        validarFechas(fechaInicio, fechaFin);
        validarCliente(cliente);
        validarAlojamiento(alojamiento);

        if (numHuespedes > alojamiento.getCapacidad()) {
            throw new Exception("El número de huéspedes excede la capacidad del alojamiento");
        }

        if (!verificarDisponibilidad(alojamiento.getId(), fechaInicio, fechaFin)) {
            throw new Exception("El alojamiento no está disponible en las fechas seleccionadas");
        }

        double costoTotal = calcularCostoReserva(alojamiento, fechaInicio, fechaFin);
        if (cliente.getBilletera().getSaldo() < costoTotal) {
            throw new Exception("Saldo insuficiente en la billetera");
        }

        Reserva reserva = new Reserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes);

        if (cliente.getBilletera().pagar(costoTotal)) {
            reserva.setEstado(EstadoReserva.CONFIRMADA);
            Factura factura = generarFactura(reserva);
            reserva.setFactura(factura);

            reservaRepositorio.guardar(reserva);
            enviarConfirmacionReserva(cliente, reserva);

            return reserva;
        } else {
            throw new Exception("Error al procesar el pago");
        }
    }

    @Override
    public boolean cancelarReserva(String idReserva) throws Exception {
        Optional<Reserva> optional = reservaRepositorio.buscarPorId(idReserva);

        if (optional.isEmpty()) {
            throw new Exception("Reserva no encontrada");
        }

        Reserva reserva = optional.get();

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new Exception("La reserva ya está cancelada");
        }

        if (reserva.getFechaInicio().isBefore(LocalDate.now().plusDays(2))) {
            throw new Exception("No se puede cancelar con menos de 48 horas de anticipación");
        }

        reserva.cancelar();
        reservaRepositorio.actualizar(reserva);

        // Reembolso del 80% si cancela con más de 7 días de anticipación
        if (reserva.getFechaInicio().isAfter(LocalDate.now().plusDays(7))) {
            double reembolso = reserva.getTotal() * 0.8;
            reserva.getCliente().getBilletera().recargar(reembolso);
            BilleteraServicio.getInstancia().recargarBilletera(reserva.getCliente(), reembolso);
        }

        enviarNotificacionCancelacion(reserva);
        return true;
    }

    @Override
    public Optional<Reserva> buscarReserva(String id) {
        return reservaRepositorio.buscarPorId(id);
    }

    @Override
    public List<Reserva> listarReservasPorCliente(String idCliente) {
        return reservaRepositorio.buscarPorCliente(idCliente);
    }

    @Override
    public List<Reserva> listarReservasPorAlojamiento(String idAlojamiento) {
        return reservaRepositorio.buscarPorAlojamiento(idAlojamiento);
    }

    @Override
    public boolean verificarDisponibilidad(String idAlojamiento,
                                           LocalDate fechaInicio,
                                           LocalDate fechaFin) {
        return !reservaRepositorio.existeReservaEnFecha(idAlojamiento, fechaInicio, fechaFin);
    }

    @Override
    public Factura generarFactura(Reserva reserva) {
        Factura factura = new Factura(reserva.getTotal());
        String qrCode = generadorQR.generarQR(factura.getCodigo());
        factura.setQrCode(qrCode);
        return factura;
    }

    @Override
    public boolean agregarResena(String idReserva, String comentario, int calificacion) throws Exception {
        Optional<Reserva> optional = reservaRepositorio.buscarPorId(idReserva);

        if (optional.isEmpty()) {
            throw new Exception("Reserva no encontrada");
        }

        Reserva reserva = optional.get();

        if (reserva.getEstado() != EstadoReserva.COMPLETADA) {
            throw new Exception("Solo se pueden añadir reseñas a reservas completadas");
        }

        if (calificacion < 1 || calificacion > 5) {
            throw new Exception("La calificación debe estar entre 1 y 5");
        }

        Reseña reseña = new Reseña(reserva.getCliente(), reserva.getAlojamiento(), comentario, calificacion);
        reseñaRepositorio.guardar(reseña);

        // Añadir puntos al cliente por dejar reseña (programa de fidelización)
        BilleteraServicio.getInstancia().canjearPuntos(reserva.getCliente(), 50);

        return true;
    }

    private double calcularCostoReserva(Alojamiento alojamiento,
                                        LocalDate fechaInicio,
                                        LocalDate fechaFin) {
        int numNoches = fechaInicio.until(fechaFin).getDays();
        double costoBase = alojamiento.calcularCostoTotal(numNoches);

        // Aplicar ofertas vigentes
        List<Oferta> ofertas = AlojamientoServicio.getInstancia()
                .obtenerOfertasVigentes(LocalDate.now());

        for (Oferta oferta : ofertas) {
            if (oferta.getAlojamiento().getId().equals(alojamiento.getId())) {
                costoBase *= (1 - oferta.getDescuento());
            }
        }

        return costoBase;
    }

    private void enviarConfirmacionReserva(Cliente cliente, Reserva reserva) {
        String qrCode = generadorQR.generarQR(reserva.getFactura().getCodigo());
        reserva.getFactura().setQrCode(qrCode);

        String mensaje = String.format(
                "Reserva confirmada en %s del %s al %s. Total: $%,.2f\n" +
                        "Código de reserva: %s\n\n" +
                        "Gracias por preferir BookYourStay!",
                reserva.getAlojamiento().getNombre(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getTotal(),
                reserva.getId()
        );

        new EnviadorEmail().enviarEmail(
                cliente.getEmail(),
                "Confirmación de Reserva #" + reserva.getId(),
                mensaje,
                qrCode
        );
    }

    private void enviarNotificacionCancelacion(Reserva reserva) {
        String mensaje = String.format(
                "Reserva #%s cancelada para %s del %s al %s.\n" +
                        "Monto reembolsado: $%,.2f",
                reserva.getId(),
                reserva.getAlojamiento().getNombre(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getTotal() * 0.8
        );

        new EnviadorEmail().enviarEmail(
                reserva.getCliente().getEmail(),
                "Cancelación de Reserva #" + reserva.getId(),
                mensaje,
                null
        );
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        if (fechaInicio == null || fechaFin == null) {
            throw new Exception("Las fechas no pueden ser nulas");
        }

        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new Exception("La fecha de inicio no puede ser en el pasado");
        }

        if (fechaFin.isBefore(fechaInicio) || fechaFin.isEqual(fechaInicio)) {
            throw new Exception("La fecha fin debe ser posterior a la fecha de inicio");
        }
    }

    private void validarCliente(Cliente cliente) throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente no puede ser nulo");
        }

        if (!cliente.isActivo()) {
            throw new Exception("El cliente no tiene una cuenta activa");
        }
    }

    private void validarAlojamiento(Alojamiento alojamiento) throws Exception {
        if (alojamiento == null) {
            throw new Exception("El alojamiento no puede ser nulo");
        }
    }
}