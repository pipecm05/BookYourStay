package co.edu.uniquindio.bookyourstay.modelo;

public class Billetera {
    private String clienteId;
    private double saldo;
    private int puntos;

    public Billetera() {
        this.clienteId = clienteId;
        this.saldo = 0.0;
        this.puntos = 0;
    }

    public void recargar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        saldo += monto;
    }

    public boolean pagar(double monto) {
        if (saldo >= monto) {
            saldo -= monto;
            agregarPuntos((int) (monto / 1000)); // 1 punto por cada $1000
            return true;
        }
        return false;
    }

    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public boolean canjearPuntos(int puntos) {
        if (this.puntos >= puntos) {
            this.puntos -= puntos;
            return true;
        }
        return false;
    }

    // Getters
    public double getSaldo() { return saldo; }
    public int getPuntos() { return puntos; }
    // Getter para clienteId
    public String getClienteId() {
        return this.clienteId;
    }
}