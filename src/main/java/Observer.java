/**
 * Observer interface for notification channels.
 * <p>
 * Defines the contract for sending messages to members
 * via different mediums (e.g., email, SMS, push).
 * </p>
 *
 * @author hoor
 * @version 1.0
 */
public interface Observer {
	 /**
     * Sends a notification message to the given user.
     *
     * @param member  the library member to notify
     * @param message the notification message
     */
    void notify(Member member, String message);
}
/**
 * Concrete observer that sends notifications via email.
 */
 