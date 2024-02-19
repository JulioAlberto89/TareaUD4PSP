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

    private String servidor;
    private String puerto;
    private String usuario;
    private String clave;
    private boolean TLS;
    private Session mailSession;

    public ClaseEnviar(String servidor, String puerto, String usuario, String clave, boolean TLS) {
        this.servidor = servidor;
        this.puerto = puerto;
        this.usuario = usuario;
        this.clave = clave;
        this.TLS = TLS;
    }

    public boolean conectar() {
        try
        {
            Properties prop = new Properties();
            prop.put("mail.smtp.host", servidor);
            prop.put("mail.smtp.port", puerto);
            prop.put("mail.smtp.username", usuario);
            prop.put("mail.smtp.password", clave);
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", TLS ? "true" : "false");
            prop.put("mail.debug", "true");

            mailSession = Session.getInstance(prop, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, clave);
                }
            });
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void enviarCorreo(String remitente, String destinatario, String asunto, String mensaje) throws MessagingException, IOException {
        Message message = new MimeMessage(mailSession);

        message.setFrom(new InternetAddress(remitente));
        message.setSubject(asunto);

        InternetAddress[] toEmailAddresses = InternetAddress.parse(destinatario);
        message.setRecipients(Message.RecipientType.TO, toEmailAddresses);

        // Cuerpo del correo con texto plano
        message.setText(mensaje);

        // Cuerpo del correo con HTML
        message.setContent(mensaje, "text/html");

        // Correo con archivos adjuntos
        Multipart multipart = new MimeMultipart();

        // Primera parte del mensaje
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Por favor, encuentra el archivo adjunto enviado usando Jakarta Mail");
        multipart.addBodyPart(messageBodyPart);

        // Segunda parte del mensaje con un archivo adjunto
        messageBodyPart = new MimeBodyPart();
        String filename = "File.pdf";
        messageBodyPart.attachFile(filename);
        multipart.addBodyPart(messageBodyPart);

        // Añadir el objeto multipart al mensaje
        message.setContent(multipart);

        // Correo con imágenes
        multipart = new MimeMultipart("related");
        MimeBodyPart htmlPart = new MimeBodyPart();
        String messageBody = "<p></p><img src=\"https://projects.eclipse.org/sites/default/files/36201228_22.png\" alt=\"img\" /></p>";
        htmlPart.setText(messageBody, "utf-8", "html");
        multipart.addBodyPart(htmlPart);
        message.setContent(multipart);

        // Enviar el mensaje configurado en la sesión
        Transport.send(message);
    }
}
