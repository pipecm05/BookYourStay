package co.edu.uniquindio.bookyourstay.modelo;

public class Habitacion {
    private String numero;
    private double precio;
    private int capacidad;
    private String imagen;
    private String descripcion;

    public Habitacion(String numero, double precio, int capacidad,
                      String imagen, String descripcion) {
        this.numero = numero;
        this.precio = precio;
        this.capacidad = capacidad;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}