package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class Alojamiento {
    // Propiedades observables
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty ciudad = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final ObjectProperty<TipoAlojamiento> tipo = new SimpleObjectProperty<>();
    private final FloatProperty precioNoche = new SimpleFloatProperty();
    private final IntegerProperty capacidadMax = new SimpleIntegerProperty();
    private final ListProperty<String> servicios = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Reserva> reservas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Reseña> reseñas = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final FloatProperty calificacionPromedio = new SimpleFloatProperty();
    private final BooleanProperty disponible = new SimpleBooleanProperty(true);

    // Nueva propiedad para el propietario (Usuario)
    private final ObjectProperty<Usuario> propietario = new SimpleObjectProperty<>();

    // Métodos property() para JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty ciudadProperty() { return ciudad; }
    public StringProperty descripcionProperty() { return descripcion; }
    public ObjectProperty<TipoAlojamiento> tipoProperty() { return tipo; }
    public FloatProperty precioNocheProperty() { return precioNoche; }
    public IntegerProperty capacidadMaxProperty() { return capacidadMax; }
    public ListProperty<String> serviciosProperty() { return servicios; }
    public ListProperty<Reserva> reservasProperty() { return reservas; }
    public ListProperty<Reseña> reseñasProperty() { return reseñas; }
    public FloatProperty calificacionPromedioProperty() { return calificacionPromedio; }
    public BooleanProperty disponibleProperty() { return disponible; }
    public ObjectProperty<Usuario> propietarioProperty() { return propietario; }

    // Métodos abstractos
    public abstract float calcularCostoTotal(int numNoches);

    // Métodos concretos
    protected float calcularCostoBase(int numNoches) {
        return precioNoche.get() * numNoches;
    }

    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        if (!disponible.get()) return false;

        return reservas.stream()
                .noneMatch(reserva -> reserva.estaActiva() &&
                        reserva.haySolapamiento(fechaInicio, fechaFin));
    }

    public void agregarReserva(Reserva reserva) {
        if (!estaDisponible(reserva.getFechaInicio(), reserva.getFechaFin())) {
            throw new IllegalStateException("El alojamiento no está disponible para las fechas solicitadas");
        }
        reservas.add(reserva);
    }

    public void agregarReseña(Reseña reseña) {
        if (reseña.getCalificacion() < 1 || reseña.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }

        reseñas.add(reseña);
        actualizarCalificacionPromedio();
    }

    private void actualizarCalificacionPromedio() {
        if (reseñas.isEmpty()) {
            calificacionPromedio.set(0);
            return;
        }

        calificacionPromedio.set((float) reseñas.stream()
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0));
    }

    public boolean tieneServicio(String servicio) {
        return servicios.contains(servicio);
    }

    public List<Reserva> getReservasActivas() {
        return reservas.stream()
                .filter(Reserva::estaActiva)
                .toList();
    }

    protected String generarId() {
        if (id.get() == null) {
            id.set("ALO-" + UUID.randomUUID().toString().substring(0, 8));
        }
        return id.get();
    }

    public void validar() {
        if (nombre.get() == null || nombre.get().isBlank()) {
            throw new IllegalArgumentException("El nombre del alojamiento es requerido");
        }
        if (ciudad.get() == null || ciudad.get().isBlank()) {
            throw new IllegalArgumentException("La ciudad del alojamiento es requerida");
        }
        if (precioNoche.get() <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor a cero");
        }
        if (capacidadMax.get() <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a cero");
        }
        if (propietario.get() == null) {
            throw new IllegalArgumentException("El alojamiento debe tener un propietario asignado");
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) $%,.2f/noche",
                nombre.get(), ciudad.get(), getClass().getSimpleName(), precioNoche.get());
    }

    // Constructores
    public Alojamiento(String nombre, String ciudad, Usuario propietario, String descripcion,
                       TipoAlojamiento tipo, float precioNoche, int capacidadMax) {
        this();
        this.nombre.set(nombre);
        this.ciudad.set(ciudad);
        this.propietario.set(propietario);
        this.descripcion.set(descripcion);
        this.tipo.set(tipo);
        this.precioNoche.set(precioNoche);
        this.capacidadMax.set(capacidadMax);
        generarId();
    }

    // Métodos adicionales para el propietario
    public Usuario getPropietario() {
        return propietario.get();
    }

    public void setPropietario(Usuario propietario) {
        this.propietario.set(propietario);
    }

    public boolean esPropietario(Usuario usuario) {
        return propietario.get() != null && propietario.get().equals(usuario);
    }
    public void setServicios(List<String> nuevosServicios) {
        servicios.set(FXCollections.observableArrayList(nuevosServicios));
    }
    public String getId() {
        return id.get();
    }
    public void setTipo(TipoAlojamiento tipo) {
        this.tipo.set(tipo); // Usando la propiedad JavaFX
    }
    public String getNombre() {
        return nombre.get(); // Devuelve el String, no el StringProperty
    }

    public String getCiudad() {
        return ciudad.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setCiudad(String ciudad) {
        this.ciudad.set(ciudad);
    }
    public void setId(String id) {
        this.id.set(id);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public void setPrecioNoche(float precioNoche) {
        this.precioNoche.set(precioNoche);
    }

    public void setCapacidadMax(int capacidadMax) {
        this.capacidadMax.set(capacidadMax);
    }
}