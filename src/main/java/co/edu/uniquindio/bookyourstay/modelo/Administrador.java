package co.edu.uniquindio.bookyourstay.modelo;

public class Administrador extends Usuario {
    private static final String ADMIN_EMAIL = "admin@bookyourstay.com";
    private static final String ADMIN_PASSWORD = "Admin123";

    public Administrador() {
        super("0000000000", "Administrador", "0000000000",
                ADMIN_EMAIL, ADMIN_PASSWORD);
        setActivo(true); // El admin no requiere activación
    }
}