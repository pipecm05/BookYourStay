package co.edu.uniquindio.bookyourstay.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Alojamiento {
    private Alojamiento alojamiento;
    private final String id;
    private String nombre;
    private String ciudad;
    private String descripcion;
    private String imagen;
    private double precioNoche;
    private int capacidad;
    private List<String> servicios;
    private List<Reseña> reseñas;
    private List<Oferta> ofertas;

    public Alojamiento(String nombre, String ciudad, String descripcion,
                       String imagen, double precioNoche, int capacidad) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioNoche = precioNoche;
        this.capacidad = capacidad;
        this.servicios = new ArrayList<>();
        this.reseñas = new ArrayList<>();
        this.ofertas = new ArrayList<>();
        this.alojamiento = alojamiento;
    }
    public static Alojamiento crearAlojamiento(String tipo, String nombre, String ciudad,
                                               String descripcion, String imagen,
                                               double precioNoche, int capacidad) {
        return switch (tipo.toLowerCase()) {
            case "casa" -> new Casa(nombre, ciudad, descripcion, imagen, precioNoche, capacidad, 0);
            case "apartamento" -> new Apartamento(nombre, ciudad, descripcion, imagen, precioNoche, capacidad, 0);
            case "hotel" -> new Hotel(nombre, ciudad, descripcion, imagen, precioNoche, capacidad);
            default -> throw new IllegalArgumentException("Tipo de alojamiento no válido: " + tipo);
        };
    }
    public Alojamiento getAlojamiento() {
        return alojamiento;
    }
    // Método para calcular costo
    public abstract double calcularCostoTotal(int numNoches);

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public double getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(double precioNoche) { this.precioNoche = precioNoche; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public List<String> getServicios() { return servicios; }
    public List<Reseña> getReseñas() { return reseñas; }
    public List<Oferta> getOfertas() { return ofertas; }

}