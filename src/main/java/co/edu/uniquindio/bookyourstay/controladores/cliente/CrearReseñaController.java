package co.edu.uniquindio.bookyourstay.controladores.cliente;

import co.edu.uniquindio.bookyourstay.modelo.Reseña;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCalificacion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CrearReseñaController {

    @FXML
    private TextField tfCliente;

    @FXML
    private TextField tfAlojamiento;

    @FXML
    private Spinner<Integer> spinnerCalificacion;

    @FXML
    private TextArea taComentario;

    @FXML
    private CheckBox cbRecomendar;

    @FXML
    private Label labelMensaje;

    // Lista compartida para almacenar reseñas, puede venir de un servicio o singleton
    private static final javafx.collections.ObservableList<Reseña> listaReseñas = javafx.collections.FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        spinnerCalificacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5));
    }

    @FXML
    private void agregarReseña() {
        String cliente = tfCliente.getText().trim();
        String alojamiento = tfAlojamiento.getText().trim();
        int calificacion = spinnerCalificacion.getValue();
        String comentario = taComentario.getText().trim();
        boolean recomendar = cbRecomendar.isSelected();

        if (cliente.isEmpty() || alojamiento.isEmpty() || comentario.length() < 20) {
            labelMensaje.setText("Complete todos los campos correctamente (comentario mínimo 20 caracteres).");
            return;
        }

        // Crear cliente
        co.edu.uniquindio.bookyourstay.modelo.Cliente nuevoCliente = new co.edu.uniquindio.bookyourstay.modelo.Cliente();
        nuevoCliente.setNombre(cliente);

        // Crear alojamiento concreto (Casa como ejemplo)
        co.edu.uniquindio.bookyourstay.modelo.Casa nuevaCasa = new co.edu.uniquindio.bookyourstay.modelo.Casa();
        nuevaCasa.setNombre(alojamiento);
        // Si hay atributos obligatorios, inicialízalos aquí
        // Por ejemplo: nuevaCasa.setId("CASA-" + UUID.randomUUID().toString().substring(0, 8));

        // Crear reseña
        Reseña r = new Reseña();
        r.setCliente(nuevoCliente);
        r.setAlojamiento(nuevaCasa);
        r.setCalificacion(calificacion);
        r.setComentario(comentario);
        r.setRecomendaria(recomendar);
        r.setTipoCalificacion(TipoCalificacion.ESTANDAR);

        listaReseñas.add(r);

        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText("Reseña agregada correctamente.");

        // Limpiar campos
        tfCliente.clear();
        tfAlojamiento.clear();
        spinnerCalificacion.getValueFactory().setValue(5);
        taComentario.clear();
        cbRecomendar.setSelected(false);
    }
}