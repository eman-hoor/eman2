
import java.time.LocalDate;
import java.util.List;

public class ReminderService {
    private final EmailService emailService;

    public ReminderService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendReminders(List<Book> books, MemberService memberService) {
        for (Member member : memberService.getAllMembers()) {
            long overdueCount = books.stream()
                .filter(b -> member.getMemberId().equals(b.getBorrowedBy())
                          && b.getDueDate() != null
                          && b.getDueDate().isBefore(LocalDate.now()))
                .count();

            if (overdueCount > 0) {
                String subject = "Library Reminder";
                String body = "You have " + overdueCount + " overdue book(s).";
                emailService.sendEmail(member.getContact(), subject, body);
            }
        }
    } 
}
