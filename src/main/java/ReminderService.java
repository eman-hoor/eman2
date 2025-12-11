
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Service class that sends reminders to members with overdue items.
 * <p>
 * Uses the Observer pattern to support multiple notification channels
 * (e.g., email, SMS, push). Each registered observer will be notified
 * when overdue items are detected.
 * </p>
 *
 * @author hoor
 * @version 1.0
 */
public class ReminderService {
	/** List of registered observers (notification channels). */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Registers a new notification observer.
     *
     * @param observer the observer to add
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    /**
     * Sends reminders to all members with overdue books.
     * Each registered observer will be notified.
     *
     * @param books          the list of all books
     * @param memberService  the member service providing access to members
     */
    public void sendReminders(List<Book> books, MemberService memberService) {
        for (Member member : memberService.getAllMembers()) {
            long overdueCount = books.stream()
                .filter(b -> member.getMemberId().equals(b.getBorrowedBy())
                          && b.getDueDate() != null
                          && b.getDueDate().isBefore(LocalDate.now()))
                .count();

            if (overdueCount > 0) {
                String message = "You have " + overdueCount + " overdue book(s).";
                for (Observer observer : observers) {
                    observer.notify(member, message);
                }
            }
        }
    }
}