package co.edu.uniquindio.bookyourstay.utilidades;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import java.io.ByteArrayOutputStream;

public class GeneradorQR {
    public String generarQR(String contenido) {
        ByteArrayOutputStream stream = QRCode.from(contenido)
                .withSize(250, 250)
                .to(ImageType.PNG)
                .stream();

        // Convertir a Base64 para fácil almacenamiento
        return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(stream.toByteArray());
    }
}