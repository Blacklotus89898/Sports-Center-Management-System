package ca.mcgill.ecse321.scs.service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    // Sender's email address
    private static final String from = "info@sportscenter.tech";

    private static final String username = "song.yang.chen89898@gmail.com";

    private static final String password = "fqiz zljp yatd dvpa"; //app password

    public static void sendEmail(String destination, String type) {
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination)); //replace with destination

            // Setting the email subject
            message.setSubject("SCS Confirmation Email");

            // Setting the email content
            switch (type) {
                case "registration":
                    message.setText("Dear customer,\n\n Registration success \n\n Thanks for choosing SCS");
                    break;
                case "login":
                    message.setText("Dear customer,\n\n Login alert \n\n Thanks for choosing SCS");
                    break;
                case "creation":
                    message.setText("Dear customer,\n\n Account creation success \n\n Thanks for choosing SCS");
                    break;
                case "cancellation":
                    message.setText("Dear customer,\n\n Registration cancelled \n\n Thanks for choosing SCS");
                    break;
            
                default:
                    break;
            }

            // Sending the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
