package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoUsuario;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String contraseña;
    private String telefono;
    private String cedula;
    private LocalDate fechaRegistro;
    private boolean activo;
    private TipoUsuario tipoUsuario;
    private String direccion;
    private String fotoPerfilUrl;

    public Usuario() {
        this.id = "USR-" + UUID.randomUUID().toString().substring(0, 8);
        this.fechaRegistro = LocalDate.now();
        this.activo = true;
        this.tipoUsuario = TipoUsuario.REGULAR;
    }

    /**
     * Valida los datos básicos del usuario
     * @throws IllegalArgumentException si algún dato no es válido
     */
    public void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
        if (contraseña == null || contraseña.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (telefono == null || !telefono.matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("El teléfono debe tener 10 dígitos");
        }
        if (cedula == null || !cedula.matches("^[0-9]{8,15}$")) {
            throw new IllegalArgumentException("La cédula no es válida");
        }
    }

    /**
     * Actualiza la información básica del usuario
     */
    public void actualizarInformacion(String nombre, String telefono, String direccion) {
        if (nombre != null && !nombre.isBlank()) {
            this.nombre = nombre;
        }
        if (telefono != null && telefono.matches("^[0-9]{10}$")) {
            this.telefono = telefono;
        }
        this.direccion = direccion;
    }

    /**
     * Cambia la contraseña del usuario
     */
    public void cambiarContraseña(String contraseñaActual, String nuevaContraseña) {
        if (!this.contraseña.equals(contraseñaActual)) {
            throw new IllegalArgumentException("La contraseña actual no coincide");
        }
        if (nuevaContraseña == null || nuevaContraseña.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres");
        }
        this.contraseña = nuevaContraseña;
    }

    /**
     * Activa o desactiva la cuenta del usuario
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Verifica si el usuario puede realizar una acción
     */
    public boolean puedeRealizarAccion() {
        return activo;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", nombre, email, tipoUsuario);
    }

    // Builder mejorado para Usuario
    public static abstract class UsuarioBuilder<B extends UsuarioBuilder<B>> {
        protected String id;
        protected String nombre;
        protected String email;
        protected String contraseña;
        protected String telefono;
        protected String cedula;
        protected String direccion;
        protected String fotoPerfilUrl;

        public B id(String id) {
            this.id = id;
            return self();
        }

        public B nombre(String nombre) {
            this.nombre = nombre;
            return self();
        }

        public B email(String email) {
            this.email = email;
            return self();
        }

        public B contraseña(String contraseña) {
            this.contraseña = contraseña;
            return self();
        }

        public B telefono(String telefono) {
            this.telefono = telefono;
            return self();
        }

        public B cedula(String cedula) {
            this.cedula = cedula;
            return self();
        }

        public B direccion(String direccion) {
            this.direccion = direccion;
            return self();
        }

        public B fotoPerfilUrl(String fotoPerfilUrl) {
            this.fotoPerfilUrl = fotoPerfilUrl;
            return self();
        }

        protected abstract B self();

        public abstract Usuario build();
    }

    public static UsuarioBuilder<?> builder() {
        return new UsuarioBuilderImpl();
    }

    private static class UsuarioBuilderImpl extends UsuarioBuilder<UsuarioBuilderImpl> {
        @Override
        protected UsuarioBuilderImpl self() {
            return this;
        }

        @Override
        public Usuario build() {
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setContraseña(contraseña);
            usuario.setTelefono(telefono);
            usuario.setCedula(cedula);
            usuario.setDireccion(direccion);
            usuario.setFotoPerfilUrl(fotoPerfilUrl);
            return usuario;
        }
    }
}
