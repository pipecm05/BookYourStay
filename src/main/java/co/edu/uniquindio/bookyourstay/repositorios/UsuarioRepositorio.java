package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UsuarioRepositorio {
    private final List<Usuario> usuarios = new ArrayList<>();

    /**
     * Guarda un usuario en el repositorio
     * @param usuario El usuario a guardar
     * @throws IllegalArgumentException si el usuario es nulo, o ya existe por email o cédula
     */
    public void guardarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (existePorEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        if (existePorCedula(usuario.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + usuario.getCedula());
        }
        usuarios.add(usuario);
    }

    /**
     * Busca un usuario por email
     * @param email Email a buscar
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Busca un usuario por cédula
     * @param cedula Cédula a buscar
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> buscarPorCedula(String cedula) {
        return usuarios.stream()
                .filter(u -> u.getCedula().equals(cedula))
                .findFirst();
    }

    /**
     * Obtiene todos los usuarios registrados
     * @return Lista inmutable de usuarios
     */
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    /**
     * Busca usuarios por tipo
     * @param tipo Tipo de usuario (REGULAR, PREMIUM)
     * @return Lista de usuarios del tipo especificado
     */
    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        return usuarios.stream()
                .filter(u -> u.getTipoUsuario() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Busca usuarios activos/inactivos
     * @param activo Estado de actividad
     * @return Lista de usuarios según estado
     */
    public List<Usuario> buscarPorEstado(boolean activo) {
        return usuarios.stream()
                .filter(u -> u.isActivo() == activo)
                .collect(Collectors.toList());
    }

    /**
     * Elimina un usuario del sistema
     * @param email Email del usuario a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarUsuario(String email) {
        return usuarios.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Actualiza un usuario existente
     * @param usuarioActualizado Usuario con datos actualizados
     * @throws IllegalArgumentException si el usuario no existe
     */
    public void actualizarUsuario(Usuario usuarioActualizado) {
        Usuario existente = buscarPorEmail(usuarioActualizado.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        int index = usuarios.indexOf(existente);
        usuarios.set(index, usuarioActualizado);
    }

    /**
     * Verifica si existe un usuario con el email
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    public boolean existePorEmail(String email) {
        return usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Verifica si existe un usuario con la cédula
     * @param cedula Cédula a verificar
     * @return true si existe, false si no
     */
    public boolean existePorCedula(String cedula) {
        return usuarios.stream()
                .anyMatch(u -> u.getCedula().equals(cedula));
    }

    /**
     * Busca usuarios que cumplan un criterio personalizado
     * @param criterio Predicado para filtrar
     * @return Lista de usuarios que cumplen el criterio
     */
    public List<Usuario> buscarPorCriterio(Predicate<Usuario> criterio) {
        return usuarios.stream()
                .filter(criterio)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta el total de usuarios registrados
     * @return Número total de usuarios
     */
    public long contarUsuarios() {
        return usuarios.size();
    }

    /**
     * Autentica un usuario por email y contraseña
     * @param email Email del usuario
     * @param contraseña Contraseña a verificar
     * @return Optional con el usuario si las credenciales son válidas
     */
    public Optional<Usuario> autenticarUsuario(String email, String contraseña) {
        return buscarPorEmail(email)
                .filter(u -> u.getContraseña().equals(contraseña))
                .filter(Usuario::isActivo);
    }

    /**
     * Obtiene los últimos usuarios registrados
     * @param limite Cantidad máxima de usuarios a devolver
     * @return Lista de los últimos usuarios registrados
     */
    public List<Usuario> obtenerUltimosRegistrados(int limite) {
        return usuarios.stream()
                .sorted((u1, u2) -> u2.getFechaRegistro().compareTo(u1.getFechaRegistro()))
                .limit(limite)
                .collect(Collectors.toList());
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
}