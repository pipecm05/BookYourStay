module co.edu.uniquindio.bookyourstay {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.google.gson;
    requires java.desktop;

    // Permitir reflexión desde FXML en los paquetes que contienen controladores
    opens co.edu.uniquindio.bookyourstay to javafx.fxml;
    opens co.edu.uniquindio.bookyourstay.controladores to javafx.fxml;
    opens co.edu.uniquindio.bookyourstay.controladores.cliente to javafx.fxml;
    opens co.edu.uniquindio.bookyourstay.controladores.propietario to javafx.fxml;
    opens co.edu.uniquindio.bookyourstay.controladores.administrador to javafx.fxml;

    // Exportar paquetes necesarios
    exports co.edu.uniquindio.bookyourstay;
    exports co.edu.uniquindio.bookyourstay.controladores;
    exports co.edu.uniquindio.bookyourstay.controladores.cliente;
    exports co.edu.uniquindio.bookyourstay.controladores.propietario;
    exports co.edu.uniquindio.bookyourstay.controladores.administrador;
}