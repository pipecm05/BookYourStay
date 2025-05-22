package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCuenta;
import co.edu.uniquindio.bookyourstay.repositorios.UsuarioRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class UsuarioServicio {

    private static UsuarioServicio instancia;

    private final UsuarioRepositorio usuarioRepositorio;

    // Constructor privado para singleton
    private UsuarioServicio() {
        this.usuarioRepositorio = new UsuarioRepositorio();
    }
    public Usuario registrarUsuario(Usuario usuario) throws IllegalArgumentException {
        validarDatosUsuario(usuario);
        verificarExistenciaPrevia(usuario.getEmail(), usuario.getCedula());

        usuario.setActivo(true);

        // Si no tiene tipoCuenta, le asignamos REGULAR por defecto
        if (usuario.getTipoCuenta() == null) {
            usuario.setTipoCuenta(TipoCuenta.REGULAR);
        }

        usuarioRepositorio.guardarUsuario(usuario);
        return usuario;
    }
    // Método para obtener la instancia única
    public static UsuarioServicio obtenerInstancia() {
        if (instancia == null) {
            instancia = new UsuarioServicio();
        }
        return instancia;
    }

    public Cliente registrarCliente(Cliente cliente) throws IllegalArgumentException {
        validarDatosCliente(cliente);
        verificarExistenciaPrevia(cliente.getEmail(), cliente.getCedula());

        cliente.setActivo(true);
        cliente.setTipoCuenta(TipoCuenta.REGULAR);
        usuarioRepositorio.guardarUsuario(cliente);
        return cliente;
    }

    public Usuario iniciarSesion(String email, String contraseña) throws IllegalArgumentException, IllegalStateException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas"));

        if (!usuario.isActivo()) {
            throw new IllegalStateException("La cuenta está inactiva");
        }

        if (!usuario.getContraseña().equals(contraseña)) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        return usuario;
    }

    public Usuario actualizarUsuario(Usuario usuarioActualizado) throws NoSuchElementException, IllegalArgumentException {
        validarDatosUsuario(usuarioActualizado);
        Usuario existente = obtenerUsuario(usuarioActualizado.getId());

        usuarioActualizado.setEmail(existente.getEmail());
        usuarioActualizado.setCedula(existente.getCedula());

        usuarioRepositorio.actualizarUsuario(usuarioActualizado);
        return usuarioActualizado;
    }

    public Usuario cambiarEstadoUsuario(String usuarioId, boolean activo) throws NoSuchElementException {
        Usuario usuario = obtenerUsuario(usuarioId);
        usuario.setActivo(activo);
        usuarioRepositorio.actualizarUsuario(usuario);
        return usuario;
    }

    public Usuario obtenerUsuario(String usuarioId) throws NoSuchElementException {
        return usuarioRepositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId));
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepositorio.listarTodos();
    }

    public List<Usuario> listarUsuariosPorTipo(TipoCuenta tipo) {
        return usuarioRepositorio.buscarPorTipo(tipo);
    }

    public Usuario cambiarContraseña(String usuarioId, String viejaContraseña,
                                     String nuevaContraseña) throws NoSuchElementException, IllegalArgumentException {
        validarContraseña(nuevaContraseña);
        Usuario usuario = obtenerUsuario(usuarioId);

        if (!usuario.getContraseña().equals(viejaContraseña)) {
            throw new IllegalArgumentException("La contraseña actual no es correcta");
        }

        usuario.setContraseña(nuevaContraseña);
        usuarioRepositorio.actualizarUsuario(usuario);
        return usuario;
    }

    private void validarDatosCliente(Cliente cliente) throws IllegalArgumentException {
        if (cliente == null) {
            throw new IllegalArgumentException("Los datos del cliente no pueden ser nulos");
        }
        validarDatosUsuario(cliente);

        if (cliente.getFechaNacimiento() == null || cliente.getFechaNacimiento().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("El cliente debe ser mayor de edad");
        }
    }

    private void validarDatosUsuario(Usuario usuario) throws IllegalArgumentException {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (usuario.getEmail() == null || !usuario.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
        if (usuario.getCedula() == null || usuario.getCedula().isBlank()) {
            throw new IllegalArgumentException("La cédula es requerida");
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
    }

    private void validarContraseña(String contraseña) throws IllegalArgumentException {
        if (contraseña == null || contraseña.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
    }

    private void verificarExistenciaPrevia(String email, String cedula) throws IllegalArgumentException {
        if (usuarioRepositorio.buscarPorEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (usuarioRepositorio.buscarPorCedula(cedula).isPresent()) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }
    }
}