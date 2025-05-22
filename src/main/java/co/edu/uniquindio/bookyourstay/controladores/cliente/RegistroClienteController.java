package co.edu.uniquindio.bookyourstay.controladores.cliente;


import co.edu.uniquindio.bookyourstay.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegistroClienteController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefono;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnVolver;

    private Stage dialogStage;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        // Asignar acciones a los botones
        btnRegistrarse.setOnAction(event -> handleRegistrarse());
        btnVolver.setOnAction(event -> handleVolver());
    }

    private void handleRegistrarse() {
        if (validarCampos()) {
            // Aquí puedes guardar los datos, por ejemplo en la base o lista
            System.out.println("Cliente registrado:");
            System.out.println("Nombre: " + txtNombre.getText());
            System.out.println("Cédula: " + txtCedula.getText());
            System.out.println("Email: " + txtEmail.getText());
            System.out.println("Teléfono: " + txtTelefono.getText());

            mostrarAlerta(AlertType.INFORMATION, "Registro exitoso", null, "El cliente fue registrado correctamente.");

            dialogStage.close();
        }
    }

    private void handleVolver() {
        dialogStage.close();
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String email = txtEmail.getText();
        String telefono = txtTelefono.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (nombre == null || nombre.isEmpty()) {
            mostrarAlerta(AlertType.ERROR, "Error de validación", null, "El nombre es obligatorio.");
            return false;
        }
        if (cedula == null || cedula.isEmpty()) {
            mostrarAlerta(AlertType.ERROR, "Error de validación", null, "La cédula es obligatoria.");
            return false;
        }
        if (email == null || email.isEmpty() || !email.contains("@")) {
            mostrarAlerta(AlertType.ERROR, "Error de validación", null, "El email es obligatorio y debe contener '@'.");
            return false;
        }
        if (telefono == null || telefono.isEmpty()) {
            mostrarAlerta(AlertType.ERROR, "Error de validación", null, "El teléfono es obligatorio.");
            return false;
        }
        if (password == null || password.isEmpty()) {
            mostrarAlerta(AlertType.ERROR, "Error de validación", null, "La contraseña es obligatoria.");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            mostrarAlerta(AlertType.ERROR, "Error de validación", null, "Las contraseñas no coinciden.");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String header, String contenido) {
        Alert alert = new Alert(tipo);
        alert.initOwner(dialogStage);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}