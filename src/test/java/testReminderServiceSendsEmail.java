import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

	    // Mock the Observer (EmailNotifier)
	    Observer mockObserver = Mockito.mock(Observer.class);

	    ReminderService reminderService = new ReminderService();
	    reminderService.addObserver(mockObserver);

	    reminderService.sendReminders(books, memberService);

	    // Verify that the observer was notified with the correct message
	    Mockito.verify(mockObserver, times(1))
	           .notify(eq(m), eq("You have 1 overdue book(s)."));
	}
	
	@Test
	void testReminderServiceSendsEmailsToMultipleMembers() {
	    MemberService memberService = new MemberService();
	    Member alice = new Member("M1", "Alice", "alice@example.com");
	    Member bob = new Member("M2", "Bob", "bob@example.com");
	    memberService.addMember(alice);
	    memberService.addMember(bob);

	    Book b1 = new Book("1984", "George Orwell", "12345");
	    b1.borrow("M1");
	    b1.setDueDate(LocalDate.now().minusDays(5));

	    Book b2 = new Book("Brave New World", "Aldous Huxley", "67890");
	    b2.borrow("M1");
	    b2.setDueDate(LocalDate.now().minusDays(3));

	    Book b3 = new Book("The Hobbit", "J.R.R. Tolkien", "11111");
	    b3.borrow("M2");
	    b3.setDueDate(LocalDate.now().minusDays(2));

	    Book b4 = new Book("Clean Code", "Robert C. Martin", "22222");
	    b4.borrow("M2");
	    b4.setDueDate(LocalDate.now().plusDays(10)); // not overdue

	    List<Book> books = List.of(b1, b2, b3, b4);

	    // Mock the Observer
	    Observer mockObserver = Mockito.mock(Observer.class);

	    ReminderService reminderService = new ReminderService();
	    reminderService.addObserver(mockObserver);

	    reminderService.sendReminders(books, memberService);

	    // Verify Alice got notified about 2 overdue books
	    verify(mockObserver, times(1))
	        .notify(eq(alice), eq("You have 2 overdue book(s)."));

	    // Verify Bob got notified about 1 overdue book
	    verify(mockObserver, times(1))
	        .notify(eq(bob), eq("You have 1 overdue book(s)."));

	    // Verify no other notifications
	    verifyNoMoreInteractions(mockObserver);
	}
	@Test
    void testNoRemindersSentWhenNoOverdueBooks() {
        MemberService memberService = new MemberService();
        Member m = new Member("M1", "Alice", "alice@example.com");
        memberService.addMember(m);

        Book b = new Book("1984", "George Orwell", "12345");
        b.borrow("M1");
        b.setDueDate(java.time.LocalDate.now().plusDays(5)); // not overdue

        List<Book> books = List.of(b);

        Observer mockObserver = mock(Observer.class);

        ReminderService reminderService = new ReminderService();
        reminderService.addObserver(mockObserver);

        reminderService.sendReminders(books, memberService);

        // Verify observer was never called
        verifyNoInteractions(mockObserver);
    }
	
	@Test
	void testMultipleObserversAreNotified() {
	    MemberService memberService = new MemberService();
	    Member m = new Member("M1", "Alice", "alice@example.com");
	    memberService.addMember(m);

	    Book b = new Book("1984", "George Orwell", "12345");
	    b.borrow("M1");
	    b.setDueDate(java.time.LocalDate.now().minusDays(3)); // overdue

	    List<Book> books = List.of(b);

	    Observer mockObserver1 = mock(Observer.class);
	    Observer mockObserver2 = mock(Observer.class);

	    ReminderService reminderService = new ReminderService();
	    reminderService.addObserver(mockObserver1);
	    reminderService.addObserver(mockObserver2);

	    reminderService.sendReminders(books, memberService);

	    verify(mockObserver1, times(1)).notify(eq(m), eq("You have 1 overdue book(s)."));
	    verify(mockObserver2, times(1)).notify(eq(m), eq("You have 1 overdue book(s)."));
	}

	 /*@Test
    void testReminderServiceSendsEmailsToMultipleMembers() {
        // Setup MemberService with two members
        MemberService memberService = new MemberService();
        Member alice = new Member("M1", "Alice", "alice@example.com");
        Member bob = new Member("M2", "Bob", "bob@example.com");
        memberService.addMember(alice);
        memberService.addMember(bob);

        // Books: Alice has 2 overdue, Bob has 1 overdue
        Book b1 = new Book("1984", "George Orwell", "12345");
        b1.borrow("M1");
        b1.setDueDate(LocalDate.now().minusDays(5)); // overdue

        Book b2 = new Book("Brave New World", "Aldous Huxley", "67890");
        b2.borrow("M1");
        b2.setDueDate(LocalDate.now().minusDays(3)); // overdue

        Book b3 = new Book("The Hobbit", "J.R.R. Tolkien", "11111");
        b3.borrow("M2");
        b3.setDueDate(LocalDate.now().minusDays(2)); // overdue

        Book b4 = new Book("Clean Code", "Robert C. Martin", "22222");
        b4.borrow("M2");
        b4.setDueDate(LocalDate.now().plusDays(10)); // not overdue

        List<Book> books = List.of(b1, b2, b3, b4);

        // Mock EmailService
        EmailService mockEmailService = Mockito.mock(EmailService.class);
        ReminderService reminderService = new ReminderService(mockEmailService);

        // Run reminder logic
        reminderService.sendReminders(books, memberService);

        // Verify Alice got an email about 2 overdue books
        verify(mockEmailService, times(1))
            .sendEmail(eq("alice@example.com"),
                       eq("Library Reminder"),
                       eq("You have 2 overdue book(s)."));

        // Verify Bob got an email about 1 overdue book
        verify(mockEmailService, times(1))
            .sendEmail(eq("bob@example.com"),
                       eq("Library Reminder"),
                       eq("You have 1 overdue book(s)."));

        // Verify no other emails were sent
        verifyNoMoreInteractions(mockEmailService);}
    

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
*/
}
