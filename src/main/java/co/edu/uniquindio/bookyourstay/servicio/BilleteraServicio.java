package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.repositorio.BilleteraRepositorio;
import co.edu.uniquindio.bookyourstay.utilidades.Notificador;

public class BilleteraServicio implements IBilleteraServicio {
    private static BilleteraServicio instancia;
    private final BilleteraRepositorio billeteraRepositorio;
    private final Notificador notificador;

    private BilleteraServicio() {
        this.billeteraRepositorio = BilleteraRepositorio.getInstancia();
        this.notificador = new Notificador();
    }

    public static BilleteraServicio getInstancia() {
        if (instancia == null) {
            instancia = new BilleteraServicio();
        }
        return instancia;
    }

    @Override
    public boolean recargarBilletera(Cliente cliente, double monto) throws Exception {
        validarCliente(cliente);

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        if (monto > 10000000) { // Límite de recarga de 10 millones
            throw new IllegalArgumentException("El monto excede el límite máximo de recarga");
        }

        cliente.getBilletera().recargar(monto);
        billeteraRepositorio.actualizar(cliente.getBilletera());

        // Notificar al cliente
        notificador.enviarNotificacion(
                cliente.getEmail(),
                "Recarga exitosa",
                String.format("Se ha recargado $%,.2f a tu billetera. Nuevo saldo: $%,.2f",
                        monto, cliente.getBilletera().getSaldo())
        );

        return true;
    }

    @Override
    public boolean pagarReserva(Cliente cliente, double monto) throws Exception {
        validarCliente(cliente);

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        if (cliente.getBilletera().getSaldo() < monto) {
            throw new IllegalStateException("Saldo insuficiente en la billetera");
        }

        boolean resultado = cliente.getBilletera().pagar(monto);
        if (resultado) {
            billeteraRepositorio.actualizar(cliente.getBilletera());

            // Notificar al cliente
            notificador.enviarNotificacion(
                    cliente.getEmail(),
                    "Pago realizado",
                    String.format("Se ha debitado $%,.2f de tu billetera. Nuevo saldo: $%,.2f",
                            monto, cliente.getBilletera().getSaldo())
            );
        }
        return resultado;
    }

    @Override
    public boolean canjearPuntos(Cliente cliente, int puntos) throws Exception {
        validarCliente(cliente);

        if (puntos <= 0) {
            throw new IllegalArgumentException("Los puntos a canjear deben ser positivos");
        }

        if (puntos > cliente.getBilletera().getPuntos()) {
            throw new IllegalStateException("No tienes suficientes puntos para canjear");
        }

        boolean resultado = cliente.getBilletera().canjearPuntos(puntos);
        if (resultado) {
            billeteraRepositorio.actualizar(cliente.getBilletera());

            // Notificar al cliente
            notificador.enviarNotificacion(
                    cliente.getEmail(),
                    "Puntos canjeados",
                    String.format("Has canjeado %,d puntos. Puntos restantes: %,d",
                            puntos, cliente.getBilletera().getPuntos())
            );
        }
        return resultado;
    }

    @Override
    public double consultarSaldo(Cliente cliente) throws Exception {
        validarCliente(cliente);
        return cliente.getBilletera().getSaldo();
    }

    @Override
    public int consultarPuntos(Cliente cliente) throws Exception {
        validarCliente(cliente);
        return cliente.getBilletera().getPuntos();
    }

    @Override
    public double calcularEquivalenciaPuntos(Cliente cliente, int puntos) throws Exception {
        validarCliente(cliente);

        if (puntos <= 0) {
            throw new IllegalArgumentException("Los puntos deben ser positivos");
        }

        // 100 puntos = $10.000 de descuento
        return (puntos / 100) * 10000.0;
    }

    @Override
    public boolean transferirSaldo(Cliente origen, Cliente destino, double monto) throws Exception {
        validarCliente(origen);
        validarCliente(destino);

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        if (origen.getBilletera().getSaldo() < monto) {
            throw new IllegalStateException("Saldo insuficiente para transferencia");
        }

        // Realizar transferencia atómica
        try {
            origen.getBilletera().pagar(monto);
            destino.getBilletera().recargar(monto);

            billeteraRepositorio.actualizar(origen.getBilletera());
            billeteraRepositorio.actualizar(destino.getBilletera());

            // Notificar a ambos clientes
            notificador.enviarNotificacion(
                    origen.getEmail(),
                    "Transferencia realizada",
                    String.format("Has transferido $%,.2f a %s. Nuevo saldo: $%,.2f",
                            monto, destino.getNombre(), origen.getBilletera().getSaldo())
            );

            notificador.enviarNotificacion(
                    destino.getEmail(),
                    "Transferencia recibida",
                    String.format("Has recibido $%,.2f de %s. Nuevo saldo: $%,.2f",
                            monto, origen.getNombre(), destino.getBilletera().getSaldo())
            );

            return true;
        } catch (Exception e) {
            // Revertir cambios en caso de error
            origen.getBilletera().recargar(monto);
            destino.getBilletera().pagar(monto);
            throw new Exception("Error al realizar la transferencia", e);
        }
    }

    // Método auxiliar para validar cliente
    private void validarCliente(Cliente cliente) throws IllegalArgumentException {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        if (cliente.getBilletera() == null) {
            throw new IllegalStateException("El cliente no tiene una billetera asociada");
        }
    }
}