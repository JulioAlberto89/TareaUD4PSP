/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actividad_2;

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

    public void enviarCorreo(String usuario, String clave, String servidor, String puerto, String remitente, String mensaje) throws AddressException, MessagingException {
        final Properties prop = new Properties();
        prop.put("mail.smtp.username", usuario);
        prop.put("mail.smtp.password", clave);
        prop.put("mail.smtp.host", servidor);
        prop.put("mail.smtp.port", puerto);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS
        prop.put("mail.debug", "true");

        // Crear la sesión con las credenciales del usuario
        Session mailSession = Session.getInstance(prop, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("mail.smtp.username"), prop.getProperty("mail.smtp.password"));
            }
        });

        // Preparar el MimeMessage
        Message message = new MimeMessage(mailSession);
        // Establecer el remitente y el asunto del correo electrónico
        message.setFrom(new InternetAddress(remitente));
        message.setSubject("Sending Mail with pure Jakarta Mail API");

        // Establecer destinatarios To, CC y BCC
        InternetAddress[] toEmailAddresses = InternetAddress.parse(remitente);
        //InternetAddress[] ccEmailAddresses = InternetAddress.parse(destinatarioCC);
        //InternetAddress[] bccEmailAddresses = InternetAddress.parse(destinatarioBCC);

        message.setRecipients(Message.RecipientType.TO, toEmailAddresses);
        //message.setRecipients(Message.RecipientType.CC, ccEmailAddresses);
        //message.setRecipients(Message.RecipientType.BCC, bccEmailAddresses);

        // Cuerpo del correo con HTML que incluye una imagen
        Multipart multipart = new MimeMultipart("related");
        MimeBodyPart htmlPart = new MimeBodyPart();
        // Agregar referencia a la imagen en el cuerpo HTML <img src="cid:some-image-cid" alt="img" />
        mensaje = "<p></p><img src=\"https://projects.eclipse.org/sites/default/files/36201228_22.png\" alt=\"img\" /></p>";
        htmlPart.setText(mensaje, "utf-8", "html");
        // Agregar el BodyPart al objeto Multipart
        multipart.addBodyPart(htmlPart);
        // Establecer el contenido del mensaje
        message.setContent(multipart);

        // Enviar el mensaje configurado en la sesión
        Transport.send(message);
    }
}
/*
    public static void main(String[] args) throws AddressException, MessagingException, IOException {

        final Properties prop = new Properties();
        prop.put("mail.smtp.username", "username@gmail.com");
        prop.put("mail.smtp.password", "contraseña generada en la configuración de gmail");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS
        prop.put("mail.debug", "true");

// Create the Session with the user credentials
        Session mailSession = Session.getInstance(prop, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("mail.smtp.username"),
                        prop.getProperty("mail.smtp.password"));
            }
        });
        // Prepare the MimeMessage
        Message message = new MimeMessage(mailSession);
// Set From and subject email properties
        message.setFrom(new InternetAddress("username@gmail.com"));
        message.setSubject("Sending Mail with pure Jakarta Mail API ");

// Set to, cc & bcc recipients        
        InternetAddress[] toEmailAddresses
                = InternetAddress.parse("username@gmail.com");
        InternetAddress[] ccEmailAddresses
                = InternetAddress.parse("correo1@gmail.com");
        InternetAddress[] bccEmailAddresses
                = InternetAddress.parse("correo2@gmail.com");

        message.setRecipients(Message.RecipientType.TO, toEmailAddresses);
        message.setRecipients(Message.RecipientType.CC, ccEmailAddresses);
        message.setRecipients(Message.RecipientType.BCC, bccEmailAddresses);

//Mail body with plain Text
 /* message.setText("Hello User,"
                + "\n\n If you read this, means mail sent with Java Mail API is successful");*/

//Mail body with HTML
/* message.setContent("Just discovered that Jakarta Mail is fun and easy to use",
                "text/html");*/
//Mail with attached files
/*
    // create an instance of multipart object
        Multipart multipart = new MimeMultipart();

    // create the 1st message body part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
    // Add a plain message (HTML can also be added with setContent)
        messageBodyPart.setText("Please find the attachment sent using Jakarta Mail");
    // Add the BodyPart to the Multipart object
        multipart.addBodyPart(messageBodyPart);

    // 2nd. bodyPart with an attached file
        messageBodyPart = new MimeBodyPart();
        String filename = "File.pdf";
        messageBodyPart.attachFile(filename);
    // Add the BodyPart to the Multipart object
        multipart.addBodyPart(messageBodyPart);

    // Add the multipart object to the message
        message.setContent(multipart);
 */
//Mail with images
//An HTML code with a link referenced image
/*
        Multipart multipart = new MimeMultipart("related");
        MimeBodyPart htmlPart = new MimeBodyPart();
//add reference to your image to the HTML body <img src="cid:some-image-cid" alt="img" />
        String messageBody = "<p></p><img src=\"https://projects.eclipse.org/sites/default/files/36201228_22.png\" alt=\"img\" /></p>";
        htmlPart.setText(messageBody, "utf-8", "html");
// Add the BodyPart to the Multipart object
        multipart.addBodyPart(htmlPart);
// Add the multipart object to the message
        message.setContent(multipart);
        
       
// Send the configured message in the session
        Transport.send(message);
    }
 */
