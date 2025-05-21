package co.edu.uniquindio.bookyourstay.singleton;
import co.edu.uniquindio.bookyourstay.modelo.Administrador;

public class AdministradorSingleton {
    private static Administrador instancia;

    private AdministradorSingleton() {}

    public static Administrador getInstancia() {
        if (instancia == null) {
            instancia = new Administrador();
            instancia.setId("admin-001");
            instancia.setNombre("Admin Principal");
            instancia.setEmail("admin@bookyourstay.com");
            instancia.setContraseña("admin123");
            instancia.setTelefono("1234567890");
            instancia.setCedula("123456789");
        }
        return instancia;
    }
}