package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;

import java.util.Optional;

public interface IUsuarioServicio {
    boolean registrarCliente(String cedula, String nombre, String telefono,
                             String email, String password) throws Exception;
    Usuario iniciarSesion(String email, String password) throws Exception;
    boolean activarCuenta(String email, String codigo) throws Exception;
    void solicitarCambioPassword(String email) throws Exception;
    boolean cambiarPassword(String email, String codigo, String nuevaPassword) throws Exception;
    Optional<Usuario> buscarPorEmail(String email);
    boolean actualizarCliente(Cliente cliente) throws Exception;
    boolean eliminarCliente(String id) throws Exception;
}