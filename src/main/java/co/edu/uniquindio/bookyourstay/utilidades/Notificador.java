package co.edu.uniquindio.bookyourstay.utilidades;

public class Notificador {
    public void enviarNotificacion(String email, String asunto, String mensaje) {
        // Implementación real enviaría un email
        System.out.printf("Enviando notificación a %s: %s - %s%n", email, asunto, mensaje);

        // En producción se integraría con un servicio de email:
        // EmailService.enviar(email, asunto, mensaje);
    }
}