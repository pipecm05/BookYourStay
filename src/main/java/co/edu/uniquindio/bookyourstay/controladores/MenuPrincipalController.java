package co.edu.uniquindio.bookyourstay.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegistro;

    @FXML
    private Button btnSalir;

    @FXML
    void irLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/login.fxml"));
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void irRegistro(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/registro.fxml"));
        Stage stage = (Stage) btnRegistro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void salir(ActionEvent event) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }
}