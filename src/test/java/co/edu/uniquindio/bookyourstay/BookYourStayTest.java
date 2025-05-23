package co.edu.uniquindio.bookyourstay;
import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCalificacion;
import co.edu.uniquindio.bookyourstay.servicios.*;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookYourStayTest {

    private UsuarioServicio usuarioServicio;
    private AlojamientoServicio alojamientoServicio;
    private ReservaServicio reservaServicio;
    private ReseñaServicio reseñaServicio;
    private Cliente clientePrueba;
    private Alojamiento alojamientoPrueba;

    @BeforeEach
    public void setUp() {
        // Inicializar servicios
        usuarioServicio = UsuarioServicio.obtenerInstancia();
        alojamientoServicio = AlojamientoServicio.obtenerInstancia();
        reservaServicio = ReservaServicio.obtenerInstancia();
        reseñaServicio = ReseñaServicio.obtenerInstancia();

        // Crear usuario de prueba
        clientePrueba = new Cliente();
        clientePrueba.setId("cliente-001");
        clientePrueba.setNombre("Cliente Prueba");
        clientePrueba.setEmail("cliente@test.com");
        clientePrueba.setContraseña("password123");
        clientePrueba.setCedula("123456789");
        clientePrueba.setActivo(true);

        // Crear alojamiento de prueba
        alojamientoPrueba = new Casa("Casa Test", "Armenia", "Descripción test", 4, 150000);
        alojamientoPrueba.setId("alo-001");

        // Configurar usuario actual para pruebas
        UsuarioActual.getInstancia().setUsuario(clientePrueba);
    }

    @Test
    public void testRegistrarUsuarioExitoso() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setEmail("nuevo@test.com");
        nuevoUsuario.setContraseña("password123");
        nuevoUsuario.setCedula("987654321");

        // Act
        Usuario resultado = usuarioServicio.registrarUsuario(nuevoUsuario);

        // Assert
        assertNotNull(resultado, "El usuario registrado no debería ser nulo");
        assertEquals("Nuevo Usuario", resultado.getNombre(), "El nombre no coincide");
        assertEquals("nuevo@test.com", resultado.getEmail(), "El email no coincide");
        assertTrue(resultado.isActivo(), "El usuario debería estar activo por defecto");
    }

    @Test
    public void testCrearAlojamientoValido() {
        // Arrange
        String nombre = "Casa de Prueba";
        String ciudad = "Armenia";
        String descripcion = "Descripción de prueba";
        float precioNoche = 200000;
        int capacidad = 6;

        // Act
        Alojamiento resultado = alojamientoServicio.crearAlojamiento(
                TipoAlojamiento.CASA, nombre, ciudad, descripcion, precioNoche, capacidad, List.of("Wifi", "Piscina"));

        // Assert
        assertNotNull(resultado, "El alojamiento no debería ser nulo");
        assertEquals(nombre, resultado.getNombre(), "El nombre no coincide");
        assertEquals(ciudad, resultado.getCiudad(), "La ciudad no coincide");
        assertEquals(precioNoche, resultado.getPrecioNoche().get(), "El precio por noche no coincide");
    }

    @Test
    public void testCrearReservaValida() {
        // Arrange
        LocalDate fechaInicio = LocalDate.now().plusDays(1);
        LocalDate fechaFin = fechaInicio.plusDays(3);
        int numHuespedes = 2;

        // Configurar billetera con saldo suficiente
        clientePrueba.getBilletera().recargarSaldo(1000000, "Depósito prueba");

        // Act
        Reserva resultado = reservaServicio.crearReserva(
                clientePrueba, alojamientoPrueba, fechaInicio, fechaFin, numHuespedes);

        // Assert
        assertNotNull(resultado, "La reserva no debería ser nulo");
        assertEquals(clientePrueba, resultado.getCliente(), "El cliente no coincide");
        assertEquals(alojamientoPrueba, resultado.getAlojamiento(), "El alojamiento no coincide");
        assertEquals(EstadoReserva.CONFIRMADA, resultado.getEstado(), "El estado debería ser CONFIRMADA");
    }

    @Test
    public void testCrearReseñaValida() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId("res-001");
        reserva.setEstado(EstadoReserva.COMPLETADA);
        reserva.setCliente(clientePrueba);
        reserva.setAlojamiento(alojamientoPrueba);

        int calificacion = 5;
        String comentario = "Excelente experiencia, muy recomendado";

        // Act
        Reseña resultado = reseñaServicio.crearReseña(
                clientePrueba, alojamientoPrueba, reserva, calificacion, comentario,
                TipoCalificacion.DETALLADA, true);

        // Assert
        assertNotNull(resultado, "La reseña no debería ser nula");
        assertEquals(calificacion, resultado.getCalificacion(), "La calificación no coincide");
        assertEquals(comentario, resultado.getComentario(), "El comentario no coincide");
        assertEquals(clientePrueba, resultado.getCliente(), "El cliente no coincide");
    }

}