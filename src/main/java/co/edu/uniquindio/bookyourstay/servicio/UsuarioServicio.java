package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.repositorio.UsuarioRepositorio;
import co.edu.uniquindio.bookyourstay.utilidades.EmailException;
import co.edu.uniquindio.bookyourstay.utilidades.EnviadorEmail;
import co.edu.uniquindio.bookyourstay.utilidades.GeneradorCodigos;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class UsuarioServicio implements IUsuarioServicio {
    private static UsuarioServicio instancia;
    private final UsuarioRepositorio usuarioRepositorio;
    private final EnviadorEmail enviadorEmail;
    private final GeneradorCodigos generadorCodigos;

    private UsuarioServicio() {
        this.usuarioRepositorio = UsuarioRepositorio.getInstancia();
        this.enviadorEmail = new EnviadorEmail();
        this.generadorCodigos = new GeneradorCodigos();
    }

    public static UsuarioServicio getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioServicio();
        }
        return instancia;
    }

    @Override
    public boolean registrarCliente(String cedula, String nombre, String telefono,
                                    String email, String password) throws Exception {
        // Validaciones básicas
        validarCampos(cedula, nombre, telefono, email, password);
        validarPassword(password);
        validarEmail(email);

        // Verificar email único
        if (usuarioRepositorio.buscarPorEmail(email).isPresent()) {
            throw new Exception("El email ya está registrado");
        }

        // Crear nuevo cliente
        Cliente cliente = new Cliente(
                cedula,
                nombre,
                telefono,
                email,
                hashPassword(password) // Encriptar contraseña
        );

        // Generar y asignar código de activación
        String codigoActivacion = generadorCodigos.generarCodigoActivacion();
        cliente.setCodigoActivacion(codigoActivacion);
        cliente.setActivo(false); // Requiere activación

        // Guardar en repositorio
        usuarioRepositorio.guardar(cliente);

        // Enviar email de activación
        try {
            enviadorEmail.enviarEmailActivacion(email, codigoActivacion);
            return true;
        } catch (EmailException e) {
            // Revertir el registro si falla el envío de email
            usuarioRepositorio.eliminar(cliente.getId());
            throw new Exception("Error al enviar email de activación. Por favor intente nuevamente.", e);
        }
    }

    // Métodos auxiliares
    private void validarEmail(String email) throws Exception {
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new Exception("El email no tiene un formato válido");
        }
    }

    private String hashPassword(String password) {
        // Implementación básica - en producción usar BCrypt o similar
        return Integer.toString(password.hashCode());
    }

    @Override
    public Usuario iniciarSesion(String email, String password) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.buscarPorEmail(email);

        if (usuarioOptional.isEmpty()) {
            throw new Exception("Credenciales incorrectas");
        }

        Usuario usuario = usuarioOptional.get();

        if (!usuario.getPassword().equals(password)) {
            throw new Exception("Credenciales incorrectas");
        }

        if (!usuario.isActivo()) {
            throw new Exception("La cuenta no está activada. Por favor verifica tu email.");
        }

        return usuario;
    }

    @Override
    public boolean activarCuenta(String email, String codigo) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.buscarPorEmail(email);

        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if (usuario.isActivo()) {
            throw new Exception("La cuenta ya está activa");
        }

        if (!usuario.getCodigoActivacion().equals(codigo)) {
            throw new Exception("Código de activación incorrecto");
        }

        usuario.setActivo(true);
        usuario.setCodigoActivacion(null); // Limpiar el código después de activar
        usuarioRepositorio.actualizar(usuario);

        return true;
    }

    @Override
    public void solicitarCambioPassword(String email) throws Exception {
        // Validar email
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email es requerido");
        }

        // Buscar usuario
        Optional<Usuario> usuarioOptional = usuarioRepositorio.buscarPorEmail(email);
        if (usuarioOptional.isEmpty()) {
            // Por seguridad no revelar que el email no existe
            return;
        }

        Usuario usuario = usuarioOptional.get();

        // Generar código temporal (válido por 1 hora)
        String codigoRecuperacion = generadorCodigos.generarCodigoRecuperacion();
        usuario.setCodigoRecuperacion(codigoRecuperacion);
        usuario.setExpiracionCodigo(LocalDateTime.now().plusHours(1));

        // Actualizar en base de datos
        usuarioRepositorio.actualizar(usuario);

        // Enviar email
        try {
            enviadorEmail.enviarEmailRecuperacion(email, codigoRecuperacion);
        } catch (EmailException e) {
            throw new Exception("Error al enviar email de recuperación. Por favor intente nuevamente.", e);
        }
    }

    @Override
    public boolean cambiarPassword(String email, String codigo, String nuevaPassword) throws Exception {
        validarCampos(email, codigo, nuevaPassword);

        Optional<Usuario> usuarioOptional = usuarioRepositorio.buscarPorEmail(email);

        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if (!codigo.equals(usuario.getCodigoRecuperacion())) {
            throw new Exception("Código de recuperación inválido");
        }

        usuario.setPassword(nuevaPassword);
        usuario.setCodigoRecuperacion(null); // Limpiar el código después de usarlo
        usuarioRepositorio.actualizar(usuario);

        return true;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepositorio.buscarPorEmail(email);
    }

    @Override
    public boolean actualizarCliente(Cliente cliente) throws Exception {
        validarCampos(
                cliente.getCedula(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getPassword()
        );

        Optional<Usuario> existente = usuarioRepositorio.buscarPorEmail(cliente.getEmail());
        if (existente.isPresent() && !existente.get().getId().equals(cliente.getId())) {
            throw new Exception("El email ya está registrado por otro usuario");
        }

        return usuarioRepositorio.actualizar(cliente) != null;
    }

    @Override
    public boolean eliminarCliente(String id) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.buscarPorId(id);

        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }

        // Verificar si tiene reservas activas
        if (tieneReservasActivas(id)) {
            throw new Exception("No se puede eliminar un cliente con reservas activas");
        }

        return usuarioRepositorio.eliminar(id);
    }

    private boolean tieneReservasActivas(String idCliente) {
        // Implementación para verificar reservas activas
        // Dependerá de tu ReservaRepositorio
        return false;
    }

    private void validarCampos(String... campos) throws Exception {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                throw new Exception("Todos los campos son obligatorios");
            }
        }
    }

    // Método para validar fortaleza de contraseña
    private void validarPassword(String password) throws Exception {
        if (password.length() < 8) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres");
        }
        // Puedes añadir más validaciones (mayúsculas, números, etc.)
    }
}