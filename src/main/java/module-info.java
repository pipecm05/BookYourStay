module co.edu.uniquindio.bookyourstay {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.google.gson;

    opens co.edu.uniquindio.bookyourstay to javafx.fxml;
    opens co.edu.uniquindio.bookyourstay.controladores to javafx.fxml;
    exports co.edu.uniquindio.bookyourstay;
    exports co.edu.uniquindio.bookyourstay.controladores;
}