package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.Cliente;

public interface IBilleteraServicio {

    /**
     * Recarga saldo en la billetera del cliente
     * @param cliente Cliente a recargar
     * @param monto Cantidad a recargar (debe ser positivo)
     * @return true si la recarga fue exitosa
     * @throws Exception Si el monto es inválido o hay error en la operación
     */
    boolean recargarBilletera(Cliente cliente, double monto) throws Exception;

    /**
     * Realiza un pago desde la billetera
     * @param cliente Cliente que realiza el pago
     * @param monto Cantidad a pagar (debe ser positivo)
     * @return true si el pago fue exitoso
     * @throws Exception Si el monto es inválido, saldo insuficiente o hay error
     */
    boolean pagarReserva(Cliente cliente, double monto) throws Exception;

    /**
     * Canjea puntos por beneficios
     * @param cliente Cliente que canjea puntos
     * @param puntos Cantidad de puntos a canjear (debe ser positivo)
     * @return true si el canje fue exitoso
     * @throws Exception Si los puntos son inválidos, insuficientes o hay error
     */
    boolean canjearPuntos(Cliente cliente, int puntos) throws Exception;

    /**
     * Consulta el saldo actual de la billetera
     * @param cliente Cliente a consultar
     * @return Saldo disponible
     * @throws Exception Si hay error al consultar
     */
    double consultarSaldo(Cliente cliente) throws Exception;

    /**
     * Consulta los puntos acumulados
     * @param cliente Cliente a consultar
     * @return Puntos disponibles
     * @throws Exception Si hay error al consultar
     */
    int consultarPuntos(Cliente cliente) throws Exception;

    /**
     * Calcula el equivalente monetario de puntos
     * @param cliente Cliente dueño de los puntos
     * @param puntos Cantidad de puntos a calcular
     * @return Valor monetario equivalente
     * @throws Exception Si los puntos son inválidos o hay error
     */
    double calcularEquivalenciaPuntos(Cliente cliente, int puntos) throws Exception;

    /**
     * Transfiere saldo entre billeteras de clientes
     * @param origen Cliente que envía el dinero
     * @param destino Cliente que recibe el dinero
     * @param monto Cantidad a transferir (debe ser positivo)
     * @return true si la transferencia fue exitosa
     * @throws Exception Si el monto es inválido, saldo insuficiente o hay error
     */
    boolean transferirSaldo(Cliente origen, Cliente destino, double monto) throws Exception;
}