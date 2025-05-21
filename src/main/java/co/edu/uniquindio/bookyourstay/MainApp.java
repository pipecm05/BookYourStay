package co.edu.uniquindio.bookyourstay;

import co.edu.uniquindio.bookyourstay.controladores.InicioSesionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BookYourStay");
        mostrarInicioSesion();
    }

    public void mostrarInicioSesion() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/InicioSesion.fxml"));
            Parent root = loader.load();

            InicioSesionController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}