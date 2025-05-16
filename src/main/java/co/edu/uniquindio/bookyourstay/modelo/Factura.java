package co.edu.uniquindio.bookyourstay.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Factura {
    private final String codigo;
    private final LocalDateTime fecha;
    private final double subtotal;
    private final double total;
    private String qrCode;

    public Factura(double total) {
        this.codigo = UUID.randomUUID().toString();
        this.fecha = LocalDateTime.now();
        this.subtotal = total;
        this.total = total;
    }

    // Getters y setters
    public String getCodigo() { return codigo; }
    public LocalDateTime getFecha() { return fecha; }
    public double getSubtotal() { return subtotal; }
    public double getTotal() { return total; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}