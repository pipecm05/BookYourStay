package co.edu.uniquindio.bookyourstay.utilidades;

import java.util.UUID;

public class GeneradorCodigos {
    public String generarCodigoActivacion() {
        return UUID.randomUUID().toString();
    }

    public String generarCodigoRecuperacion() {
        // Código alfanumérico de 8 caracteres
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}