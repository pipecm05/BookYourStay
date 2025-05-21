package co.edu.uniquindio.bookyourstay.controladores.cliente;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import co.edu.uniquindio.bookyourstay.controladores.BaseController;
import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.servicios.AlojamientoServicio;

public class BusquedaAlojamientosController extends BaseController {
    @FXML private ComboBox<String> cbCiudad;
    @FXML private ComboBox<TipoAlojamiento> cbTipoAlojamiento;
    @FXML private TextField txtPrecioMax;
    @FXML private TableView<Alojamiento> tablaAlojamientos;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colTipo;
    @FXML private TableColumn<Alojamiento, Number> colPrecio;
    @FXML private TableColumn<Alojamiento, Number> colCapacidad;

    private final AlojamientoServicio alojamientoServicio = AlojamientoServicio.obtenerInstancia();
    private Cliente cliente;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @FXML
    private void initialize() {
        configurarComboboxes();
        configurarTabla();
        cargarDatosIniciales();
    }

    private void configurarComboboxes() {
        cbCiudad.setItems(FXCollections.observableArrayList(
                "Armenia", "Bogotá", "Medellín", "Cali", "Cartagena"));
        cbTipoAlojamiento.setItems(FXCollections.observableArrayList(TipoAlojamiento.values()));
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty().asString());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioNocheProperty());
        colCapacidad.setCellValueFactory(cellData -> cellData.getValue().capacidadMaxProperty());
    }

    private void cargarDatosIniciales() {
        tablaAlojamientos.setItems(alojamientoServicio.listarAlojamientosDisponibles());
    }

    @FXML
    private void handleBuscar() {
        String ciudad = cbCiudad.getValue();
        TipoAlojamiento tipo = cbTipoAlojamiento.getValue();
        Float precioMax = txtPrecioMax.getText().isEmpty() ? null : Float.parseFloat(txtPrecioMax.getText());

        tablaAlojamientos.setItems(alojamientoServicio.buscarAlojamientos(ciudad, tipo, precioMax));
    }

    @FXML
    private void handleReservar() {
        Alojamiento seleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            mainApp.mostrarNuevaReserva(cliente, seleccionado);
        } else {
            mostrarError("Por favor seleccione un alojamiento para reservar");
        }
    }
}