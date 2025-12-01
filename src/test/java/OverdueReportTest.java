import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;

class OverdueReportTest {

	private BookService bookService;
    private CDService cdService;
    private MemberService memberService;
    
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		bookService = new BookService();
        cdService = new CDService();
        memberService = new MemberService();

        // Create a member
        Member member = new Member("M1", "Alice", "alice@example.com");
        memberService.addMember(member);

        // Overdue book (due 2 days ago)
        Book book = new Book("1984", "George Orwell", "ISBN123");
        book.borrow("M1");
        book.setDueDate(LocalDate.now().minusDays(2));
        bookService.addBook(book);

        // Overdue CD (due 3 days ago)
        CD cd = new CD("C1", "Thriller", "Michael Jackson");
        cd.borrow("M1");
        cd.setDueDate(LocalDate.now().minusDays(3));
        cdService.addCD(cd);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    void testOverdueReportShowsCorrectFines() {
        // Capture console output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // Run Case 16 logic (simplified here)
        LocalDate today = LocalDate.now();

        for (Book book : bookService.getAllBooks()) {
            if (book.isBorrowed() && book.getDueDate().isBefore(today)) {
                int overdueDays = (int) java.time.temporal.ChronoUnit.DAYS.between(book.getDueDate(), today);
                FineStrategy fineStrategy = new BookFineStrategy();
                int fine = fineStrategy.calculateFine(overdueDays);
                System.out.println("Book: " + book.getTitle() + " | Fine: " + fine + " NIS");
            }
        }

        for (CD cd : cdService.getAllCDs()) {
            if (cd.isBorrowed() && cd.getDueDate().isBefore(today)) {
                int overdueDays = (int) java.time.temporal.ChronoUnit.DAYS.between(cd.getDueDate(), today);
                FineStrategy fineStrategy = new CDFineStrategy();
                int fine = fineStrategy.calculateFine(overdueDays);
                System.out.println("CD: " + cd.getTitle() + " | Fine: " + fine + " NIS");
            }
        }

        // Restore System.out
        System.setOut(System.out);

        String reportOutput = out.toString();

        // Assertions
        assertTrue(reportOutput.contains("Book: 1984"));
        assertTrue(reportOutput.contains("Fine: 20 NIS")); // 2 days * 10
        assertTrue(reportOutput.contains("CD: Thriller"));
        assertTrue(reportOutput.contains("Fine: 60 NIS")); // 3 days * 20
    }

}
