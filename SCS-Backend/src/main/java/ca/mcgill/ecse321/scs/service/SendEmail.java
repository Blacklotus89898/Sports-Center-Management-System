package ca.mcgill.ecse321.scs.service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    public void SendEmail() {}

    public static void send(String[] args) {

        // Sender's email address
        String from = "SCS@gmail.com";
        // Recipient's email address
        String to = "song.yang.chen89898@gmail.com";
        // Sender's Gmail username
        final String username = "song.yang.chen89898@gmail.com";
        // Sender's Gmail password
        final String password = "fqiz zljp yatd dvpa"; //app password

        // Setting up properties for SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Creating a Session object
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            // Creating a MimeMessage object
            Message message = new MimeMessage(session);

            // Setting sender and recipient email addresses
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Setting the email subject
            message.setSubject("Testing JavaMail API");

            // Setting the email content
            message.setText("Hello,\n\nThis is a test email sent using JavaMail API.");

            // Sending the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
