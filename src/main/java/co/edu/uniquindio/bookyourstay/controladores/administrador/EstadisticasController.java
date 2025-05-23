package co.edu.uniquindio.bookyourstay.controladores.administrador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class EstadisticasController {

    @FXML
    private PieChart pieChartUsuarios;

    @FXML
    private BarChart<String, Number> barChartIngresos;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private void initialize() {
        cargarDatosPieChart();
        cargarDatosBarChart();
    }

    private void cargarDatosPieChart() {
        PieChart.Data clientes = new PieChart.Data("Clientes", 120);
        PieChart.Data propietarios = new PieChart.Data("Propietarios", 50);
        PieChart.Data administradores = new PieChart.Data("Administradores", 5);

        pieChartUsuarios.getData().addAll(clientes, propietarios, administradores);
    }

    private void cargarDatosBarChart() {
        XYChart.Series<String, Number> ingresosSeries = new XYChart.Series<>();
        ingresosSeries.setName("Ingresos Mensuales");

        ingresosSeries.getData().add(new XYChart.Data<>("Enero", 5000));
        ingresosSeries.getData().add(new XYChart.Data<>("Febrero", 7000));
        ingresosSeries.getData().add(new XYChart.Data<>("Marzo", 6500));
        ingresosSeries.getData().add(new XYChart.Data<>("Abril", 8000));
        ingresosSeries.getData().add(new XYChart.Data<>("Mayo", 7500));

        barChartIngresos.getData().add(ingresosSeries);
    }
    @FXML
    private void volverAInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/administrador/admin_inicio.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}