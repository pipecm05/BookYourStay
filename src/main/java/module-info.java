module co.edu.uniquindio.bookyourstay {
    requires javafx.controls;
    requires javafx.fxml;
    requires qrgen;
    requires java.mail;
    requires com.google.gson;


    opens co.edu.uniquindio.bookyourstay to javafx.fxml;
    exports co.edu.uniquindio.bookyourstay;
}