/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actividad_2;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Usuario
 */
public class ClaseEnviar {

    public String servidor;
    public String puerto;
    public String usuario;
    public String clave;
    public String remitente;
    public String destinatario;
    public String asunto;
    public String mensaje;
    public boolean TLS;

    public ClaseEnviar() {
    }

    public ClaseEnviar(String servidor, String puerto, String usuario, String clave, String remitente, String destinatario, String asunto, String mensaje, boolean TLS) {
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.clave = clave;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.TLS = TLS;
    }

    public boolean conectar() {
        Properties prop = new Properties();
        prop.put("mail.smtp.username", usuario);
        prop.put("mail.smtp.password", clave);
        prop.put("mail.smtp.host", servidor);
        prop.put("mail.smtp.port", puerto);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", String.valueOf(TLS)); // TLS
        prop.put("mail.debug", "true");

        // Crear la sesión con las credenciales del usuario
        Session mailSession = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("mail.smtp.username"), prop.getProperty("mail.smtp.password"));
            }
        });

        try {
            // Intentar conectar y enviar un mensaje en blanco para verificar si los datos son válidos
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(remitente));
            message.setSubject("Test connection");

            // Establecer destinatario
            InternetAddress[] toEmailAddresses = InternetAddress.parse(remitente);
            message.setRecipients(Message.RecipientType.TO, toEmailAddresses);

            // Enviar el mensaje
            Transport.send(message);
            return true; // Si no hay excepciones, la conexión fue exitosa
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Si hay una excepción, la conexión falló
        }
    }

    public void enviarCorreo() throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.username", usuario);
        prop.put("mail.smtp.password", clave);
        prop.put("mail.smtp.host", servidor);
        prop.put("mail.smtp.port", puerto);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", String.valueOf(TLS)); // TLS
        prop.put("mail.debug", "true");

        // Crear la sesión con las credenciales del usuario
        Session mailSession = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("mail.smtp.username"), prop.getProperty("mail.smtp.password"));
            }
        });

        // Preparar el MimeMessage
        Message message = new MimeMessage(mailSession);
        // Establecer el remitente y el asunto del correo electrónico
        message.setFrom(new InternetAddress(remitente));
        message.setSubject(asunto);

        // Establecer destinatario y cuerpo del mensaje
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setContent(mensaje, "text/html");

        // Enviar el mensaje
        Transport.send(message);
    }
}
