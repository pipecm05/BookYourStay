package co.edu.uniquindio.bookyourstay.controladores.cliente;

import co.edu.uniquindio.bookyourstay.modelo.Reseña;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCalificacion;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCuenta;
import co.edu.uniquindio.bookyourstay.servicios.AlojamientoServicio;
import co.edu.uniquindio.bookyourstay.servicios.ReseñaServicio;
import co.edu.uniquindio.bookyourstay.servicios.UsuarioServicio;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CrearReseñaController {

    @FXML private TextField tfCliente;
    @FXML private TextField tfAlojamiento;
    @FXML private Spinner<Integer> spinnerCalificacion;
    @FXML private TextArea taComentario;
    @FXML private CheckBox cbRecomendar;
    @FXML private Label labelMensaje;

    private final UsuarioServicio usuarioServicio = UsuarioServicio.obtenerInstancia();
    private final AlojamientoServicio alojamientoServicio = AlojamientoServicio.obtenerInstancia();
    private final ObservableList<Reseña> listaReseñas = ReseñaServicio.obtenerInstancia().getListaReseñas();

    @FXML
    public void initialize() {
        spinnerCalificacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5));
    }

    @FXML
    public void guardarReseña() {
        String nombreCliente = tfCliente.getText().trim();
        String nombreAlojamiento = tfAlojamiento.getText().trim();
        int calificacion = spinnerCalificacion.getValue();
        String comentario = taComentario.getText().trim();
        boolean recomendar = cbRecomendar.isSelected();

        if (nombreCliente.isEmpty() || nombreAlojamiento.isEmpty() || comentario.length() < 20) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Complete todos los campos correctamente (comentario mínimo 20 caracteres).");
            return;
        }

        Cliente cliente = buscarClientePorNombre(nombreCliente);
        if (cliente == null) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Cliente no encontrado.");
            return;
        }

        Alojamiento alojamiento = alojamientoServicio.buscarAlojamientoPorNombre(nombreAlojamiento);
        if (alojamiento == null) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Alojamiento no encontrado.");
            return;
        }

        Reseña nuevaReseña = new Reseña();
        nuevaReseña.setCliente(cliente);
        nuevaReseña.setAlojamiento(alojamiento);
        nuevaReseña.setCalificacion(calificacion);
        nuevaReseña.setComentario(comentario);
        nuevaReseña.setRecomendaria(recomendar);
        nuevaReseña.setTipoCalificacion(TipoCalificacion.ESTANDAR);

        // Agregar a la lista local
        listaReseñas.add(nuevaReseña);

        // Opcional: guardar en servicio o base de datos
        // ReseñaServicio.obtenerInstancia().agregarReseña(nuevaReseña);

        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText("Reseña guardada correctamente.");

        limpiarCampos();
    }

    private Cliente buscarClientePorNombre(String nombreCliente) {
        return usuarioServicio.listarUsuariosPorTipo(TipoCuenta.REGULAR).stream()
                .filter(u -> u instanceof Cliente)
                .map(u -> (Cliente) u)
                .filter(c -> c.getNombre().equalsIgnoreCase(nombreCliente))
                .findFirst()
                .orElse(null);
    }

    private void limpiarCampos() {
        tfCliente.clear();
        tfAlojamiento.clear();
        spinnerCalificacion.getValueFactory().setValue(5);
        taComentario.clear();
        cbRecomendar.setSelected(false);
    }
}