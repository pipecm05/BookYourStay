package co.edu.uniquindio.bookyourstay.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Usuario {
    private final String id;
    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
    private String password;
    private boolean activo;
    private String codigoActivacion;
    private String codigoRecuperacion;
    private LocalDateTime expiracionCodigo;

    public Usuario(String cedula, String nombre, String telefono,
                   String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.activo = false; // Requiere activación por email
    }

    // Getters
    public String getId() { return id; }
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isActivo() { return activo; }
    public String getCodigoActivacion() { return codigoActivacion; }
    public String getCodigoRecuperacion() { return codigoRecuperacion; }
    public LocalDateTime getExpiracionCodigo() { return expiracionCodigo; }

    // Setters
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public void setCodigoActivacion(String codigoActivacion) {
        this.codigoActivacion = codigoActivacion;
    }
    public void setCodigoRecuperacion(String codigoRecuperacion) {
        this.codigoRecuperacion = codigoRecuperacion;
    }
    public void setExpiracionCodigo(LocalDateTime expiracionCodigo) {
        this.expiracionCodigo = expiracionCodigo;
    }

    // Método para verificar si el código de recuperación ha expirado
    public boolean codigoRecuperacionValido() {
        return codigoRecuperacion != null &&
                expiracionCodigo != null &&
                LocalDateTime.now().isBefore(expiracionCodigo);
    }
}