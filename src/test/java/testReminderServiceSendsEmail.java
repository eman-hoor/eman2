import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class testReminderServiceSendsEmail {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testReminderServiceSendsEmail() {
	    MemberService memberService = new MemberService();
	    Member m = new Member("M1", "Alice", "alice@example.com");
	    memberService.addMember(m);

	    Book b = new Book("1984", "George Orwell", "12345");
	    b.borrow("M1");
	    b.setDueDate(LocalDate.now().minusDays(5)); // overdue

	    List<Book> books = List.of(b);

	    EmailService mockEmailService = Mockito.mock(EmailService.class);
	    ReminderService reminderService = new ReminderService(mockEmailService);

	    reminderService.sendReminders(books, memberService);

	    Mockito.verify(mockEmailService).sendEmail(
	        "alice@example.com",
	        "Library Reminder",
	        "You have 1 overdue book(s)."
	    );
	}

}
