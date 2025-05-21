package co.edu.uniquindio.bookyourstay.controladores.admin;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import co.edu.uniquindio.bookyourstay.controladores.BaseController;
import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.servicios.AlojamientoServicio;

public class GestionAlojamientosController extends BaseController {
    @FXML private TableView<Alojamiento> tablaAlojamientos;
    @FXML private TableColumn<Alojamiento, String> colId;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML private TableColumn<Alojamiento, TipoAlojamiento> colTipo;
    @FXML private TableColumn<Alojamiento, Number> colPrecio;

    private final AlojamientoServicio alojamientoServicio = AlojamientoServicio.obtenerInstancia();

    @FXML
    private void initialize() {
        configurarTabla();
        cargarAlojamientos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCiudad.setCellValueFactory(cellData -> cellData.getValue().ciudadProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioNocheProperty());
    }

    private void cargarAlojamientos() {
        tablaAlojamientos.setItems(alojamientoServicio.listarTodos());
    }

    @FXML
    private void handleNuevo() {
        mainApp.mostrarFormularioAlojamiento(null);
    }

    @FXML
    private void handleEditar() {
        Alojamiento seleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            mainApp.mostrarFormularioAlojamiento(seleccionado);
        } else {
            mostrarError("Por favor seleccione un alojamiento para editar");
        }
    }

    @FXML
    private void handleEliminar() {
        Alojamiento seleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            try {
                alojamientoServicio.eliminarAlojamiento(seleccionado.getId());
                cargarAlojamientos();
                mostrarInformacion("Éxito", "Alojamiento eliminado correctamente");
            } catch (Exception e) {
                mostrarError(e.getMessage());
            }
        } else {
            mostrarError("Por favor seleccione un alojamiento para eliminar");
        }
    }

    @FXML
    private void handleRefrescar() {
        cargarAlojamientos();
    }
}