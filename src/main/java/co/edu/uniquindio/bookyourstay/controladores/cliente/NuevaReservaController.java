package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import co.edu.uniquindio.bookyourstay.controladores.BaseController;
import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.servicios.ReservaServicio;

public class NuevaReservaController extends BaseController {
    @FXML private Label lblAlojamiento;
    @FXML private DatePicker dpFechaEntrada;
    @FXML private DatePicker dpFechaSalida;
    @FXML private Spinner<Integer> spHuespedes;
    @FXML private Label lblTotal;

    private final ReservaServicio reservaServicio = ReservaServicio.obtenerInstancia();
    private Cliente cliente;
    private Alojamiento alojamiento;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
        lblAlojamiento.setText(alojamiento.getNombre());
    }

    @FXML
    private void initialize() {
        configurarControles();
        configurarListeners();
        calcularTotal();
    }

    private void configurarControles() {
        spHuespedes.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, alojamiento.getCapacidadMax(), 1));

        dpFechaEntrada.setValue(LocalDate.now());
        dpFechaSalida.setValue(LocalDate.now().plusDays(1));
    }

    private void configurarListeners() {
        dpFechaEntrada.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
        dpFechaSalida.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
        spHuespedes.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
    }

    private void calcularTotal() {
        try {
            long noches = dpFechaEntrada.getValue().until(dpFechaSalida.getValue()).getDays();
            float total = alojamiento.calcularCostoTotal((int)noches);
            lblTotal.setText(String.format("$%,.2f", total));
        } catch (Exception e) {
            lblTotal.setText("Fechas inválidas");
        }
    }

    @FXML
    private void handleConfirmar() {
        try {
            Reserva reserva = reservaServicio.crearReserva(
                    cliente,
                    alojamiento,
                    dpFechaEntrada.getValue(),
                    dpFechaSalida.getValue(),
                    spHuespedes.getValue()
            );

            mostrarInformacion("Reserva exitosa", "Su reserva #" + reserva.getId() + " ha sido confirmada");
            mainApp.mostrarVistaCliente(cliente);

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }
}