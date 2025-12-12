
import java.util.logging.Logger;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
/**
 * Service class for sending emails using the Jakarta Mail API.
 * Configures SMTP settings (Gmail by default) and authenticates
 * with the provided credentials to send messages.
 *
 * <p>Example usage:</p>
 * <pre>
 *     EmailService emailService = new EmailService("yourEmail@gmail.com", "yourPassword");
 *     emailService.sendEmail("recipient@example.com", "Subject", "Message body");
 * </pre>
 *
 * @author hoor
 * @version 1.0
 */
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    /** The username (email address) used for authentication. */
    private final String username;
    /** The password or app-specific password used for authentication. */
    private final String password;
    /**
     * Constructs a new EmailService with the given credentials.
     *
     * @param username the email address used to send messages
     * @param password the password or app-specific password for authentication
     */

    public EmailService( String username, String password) {
         
        this.username = username; 
        this.password = password; 
    }
    /**
     * Sends an email message to the specified recipient.
     * Configures SMTP properties for Gmail (TLS, port 587).
     *
     * @param to      the recipient's email address
     * @param subject the subject line of the email
     * @param body    the text body of the email
     * @throws RuntimeException if sending fails due to a {@link MessagingException}
     */

    public void sendEmail(String to, String subject, String body) {
        try {
            // SMTP configuration
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // <-- important 
            // Authenticate
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Build message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject); 
            message.setText(body);

            // Send
            Transport.send(message); 

            System.out.println("Email sent successfully to " + to);

        } catch (MessagingException e) {
            LOGGER.severe("Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
 

