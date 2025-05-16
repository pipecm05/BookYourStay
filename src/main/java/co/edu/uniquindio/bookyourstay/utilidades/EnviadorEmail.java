package co.edu.uniquindio.bookyourstay.utilidades;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EnviadorEmail {
    private final String usuarioEmail;
    private final String passwordEmail;
    private final Properties propiedadesSMTP;

    public EnviadorEmail() {
        // Configuración inicial - deberías cargar estos valores desde un archivo de configuración
        this.usuarioEmail = "tu_email@bookyourstay.com";
        this.passwordEmail = "tu_password_seguro";

        this.propiedadesSMTP = new Properties();
        propiedadesSMTP.put("mail.smtp.host", "smtp.gmail.com"); // Ejemplo para Gmail
        propiedadesSMTP.put("mail.smtp.port", "587");
        propiedadesSMTP.put("mail.smtp.auth", "true");
        propiedadesSMTP.put("mail.smtp.starttls.enable", "true");
        propiedadesSMTP.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    public void enviarEmailActivacion(String emailDestino, String codigoActivacion) throws EmailException {
        String asunto = "Activa tu cuenta en BookYourStay";
        String mensaje = String.format(
                "<h1>Bienvenido a BookYourStay</h1>" +
                        "<p>Gracias por registrarte. Para activar tu cuenta, por favor ingresa el siguiente código:</p>" +
                        "<h2>%s</h2>" +
                        "<p>O haz clic <a href='https://bookyourstay.com/activar?codigo=%s&email=%s'>aquí</a> para activar directamente.</p>",
                codigoActivacion, codigoActivacion, emailDestino
        );

        enviarEmail(emailDestino, asunto, mensaje, true);
    }

    public void enviarEmailRecuperacion(String emailDestino, String codigoRecuperacion) throws EmailException {
        String asunto = "Recuperación de contraseña - BookYourStay";
        String mensaje = String.format(
                "<h1>Recuperación de contraseña</h1>" +
                        "<p>Hemos recibido una solicitud para cambiar tu contraseña.</p>" +
                        "<p>Tu código de recuperación es: <strong>%s</strong></p>" +
                        "<p>Si no solicitaste este cambio, por favor ignora este mensaje.</p>",
                codigoRecuperacion
        );

        enviarEmail(emailDestino, asunto, mensaje, true);
    }

    public void enviarEmailConfirmacionReserva(String emailDestino, String detallesReserva, String qrCode) throws EmailException {
        String asunto = "Confirmación de Reserva - BookYourStay";
        String mensaje = String.format(
                "<h1>¡Reserva Confirmada!</h1>" +
                        "<p>%s</p>" +
                        "<img src='%s' alt='Código QR de la reserva' style='width:200px;height:200px;'>" +
                        "<p>Gracias por preferir BookYourStay</p>",
                detallesReserva, qrCode
        );

        enviarEmail(emailDestino, asunto, mensaje, true);
    }

    private void enviarEmail(String destinatario, String asunto, String contenido, boolean esHTML) throws EmailException {
        try {
            Session session = Session.getInstance(propiedadesSMTP, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuarioEmail, passwordEmail);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuarioEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            if (esHTML) {
                message.setContent(contenido, "text/html; charset=utf-8");
            } else {
                message.setText(contenido);
            }

            Transport.send(message);

        } catch (MessagingException e) {
            throw new EmailException("Error al enviar el email: " + e.getMessage(), e);
        }
    }
}