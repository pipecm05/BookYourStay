package co.edu.uniquindio.bookyourstay.controladores.cliente;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.servicios.AlojamientoServicio;
import co.edu.uniquindio.bookyourstay.servicios.ReservaServicio;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuscarAlojamientosController {

    @FXML private TextField ciudadField;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private ComboBox<String> tipoAlojamientoCombo;
    @FXML private ListView<String> resultadosList;
    @FXML private Spinner<Integer> numHuespedesSpinner;
    @FXML private Button btnReservar;

    private Alojamiento alojamientoSeleccionado;
    private final AlojamientoServicio alojamientoServicio = AlojamientoServicio.obtenerInstancia();
    private final ReservaServicio reservaServicio = ReservaServicio.obtenerInstancia();

    @FXML
    public void initialize() {
        // Inicialización del ComboBox
        tipoAlojamientoCombo.setItems(FXCollections.observableArrayList("Casa", "Apartamento", "Hotel"));

        // Configuración del Spinner
        SpinnerValueFactory<Integer> factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        numHuespedesSpinner.setValueFactory(factory);

        // Deshabilitar botón de reserva inicialmente
        btnReservar.setDisable(true);

        // Listener para selección en la lista
        resultadosList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    String nombreAlojamiento = newVal.split(" - ")[0];
                    Alojamiento alojamiento = alojamientoServicio.buscarAlojamientoPorNombre(nombreAlojamiento);
                    if (alojamiento != null) {
                        this.alojamientoSeleccionado = alojamiento;
                        btnReservar.setDisable(false);

                        // Obtener el SpinnerValueFactory como IntegerSpinnerValueFactory
                        SpinnerValueFactory<Integer> valueFactory = numHuespedesSpinner.getValueFactory();
                        if (valueFactory instanceof SpinnerValueFactory.IntegerSpinnerValueFactory) {
                            ((SpinnerValueFactory.IntegerSpinnerValueFactory) valueFactory)
                                    .setMax(alojamientoSeleccionado.getCapacidadMax().get());
                        }
                    }
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se pudo cargar el alojamiento seleccionado");
                }
            }
        });
    }

    @FXML
    private void buscar() {
        String ciudad = ciudadField.getText();
        LocalDate fechaInicio = fechaInicioPicker.getValue();
        LocalDate fechaFin = fechaFinPicker.getValue();
        String tipo = tipoAlojamientoCombo.getValue();

        if (ciudad.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar una ciudad");
            return;
        }

        // Validación de fechas
        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            mostrarAlerta("Error", "Las fechas ingresadas no son válidas");
            return;
        }

        try {
            List<Alojamiento> alojamientosDisponibles = alojamientoServicio.buscarDisponibles(
                    ciudad,
                    fechaInicio,
                    fechaFin,
                    1,
                    tipo != null ? TipoAlojamiento.valueOf(tipo.toUpperCase()) : null,
                    reservaServicio
            );

            if (alojamientosDisponibles.isEmpty()) {
                mostrarAlerta("Información", "No se encontraron alojamientos disponibles con esos criterios");
                resultadosList.getItems().clear();
            } else {
                // Mapear a Strings y crear ObservableList directamente
                ObservableList<String> items = FXCollections.observableArrayList(
                        alojamientosDisponibles.stream()
                                .map(a -> String.format("%s - %s ($%.2f/noche)",
                                        a.getNombre(),
                                        a.getCiudad(),
                                        a.getPrecioNoche().get())) // Asumiendo que es una DoubleProperty
                                .collect(Collectors.toList())
                );

                resultadosList.setItems(items);
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error", "Tipo de alojamiento no válido");
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al buscar alojamientos: " + e.getMessage());
        }
    }

    @FXML
    private void reservar() {
        if (alojamientoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un alojamiento primero");
            return;
        }

        if (fechaInicioPicker.getValue() == null || fechaFinPicker.getValue() == null) {
            mostrarAlerta("Error", "Seleccione fechas de inicio y fin");
            return;
        }

        if (fechaFinPicker.getValue().isBefore(fechaInicioPicker.getValue())) {
            mostrarAlerta("Error", "La fecha de fin debe ser posterior a la de inicio");
            return;
        }

        try {
            Usuario usuario = UsuarioActual.getInstancia().getUsuario();

            if (!(usuario instanceof Cliente)) {
                mostrarAlerta("Error", "Solo los clientes pueden realizar reservas");
                return;
            }

            Cliente cliente = (Cliente) usuario;

            // Usar el método con parámetros individuales
            Reserva reserva = reservaServicio.crearReserva(
                    cliente,
                    alojamientoSeleccionado,
                    fechaInicioPicker.getValue(),
                    fechaFinPicker.getValue(),
                    numHuespedesSpinner.getValue()
            );

            mostrarAlerta("Éxito", "Reserva creada: " + reserva.getId());
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void regresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/co/edu/uniquindio/bookyourstay/vistas/cliente/cliente_inicio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ciudadField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        resultadosList.getSelectionModel().clearSelection();
        fechaInicioPicker.setValue(null);
        fechaFinPicker.setValue(null);
        numHuespedesSpinner.getValueFactory().setValue(1);
        btnReservar.setDisable(true);
        alojamientoSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}