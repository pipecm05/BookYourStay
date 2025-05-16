package co.edu.uniquindio.bookyourstay.modelo;

public class Casa extends Alojamiento {
    private double costoAseo;

    public Casa(String nombre, String ciudad, String descripcion,
                String imagen, double precioNoche, int capacidad, double costoAseo) {
        super(nombre, ciudad, descripcion, imagen, precioNoche, capacidad);
        this.costoAseo = costoAseo;
    }

    @Override
    public double calcularCostoTotal(int numNoches) {
        return (getPrecioNoche() * numNoches) + costoAseo;
    }

    // Getters y Setters
    public double getCostoAseo() { return costoAseo; }
    public void setCostoAseo(double costoAseo) { this.costoAseo = costoAseo; }
}