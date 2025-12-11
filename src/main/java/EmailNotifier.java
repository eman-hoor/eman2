/**
 * Concrete observer that sends notifications via email.
 * <p>
 * Implements the {@link Observer} interface using the
 * {@link EmailService} to deliver messages.
 * </p>
 *
 * @author hoor
 * @version 1.0
 */
public class EmailNotifier implements Observer {
	/** The email service used to send messages. */
    private final EmailService emailService;

    /**
     * Constructs an EmailNotifier with the given email service.
     *
     * @param emailService the email service used to send notifications
     */
    public EmailNotifier(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sends a notification to the member via email.
     *
     * @param member  the library member to notify
     * @param message the notification message
     */
    @Override
    public void notify(Member member, String message) {
        emailService.sendEmail(member.getContact(), "Library Reminder", message);
    }
}
