import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookTest {

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
    void testBookConstructorAndGetters() {
        Book book = new Book("1984", "George Orwell", "12345");

        assertEquals("1984", book.getTitle());
        assertEquals("George Orwell", book.getAuthor());
        assertEquals("12345", book.getIsbn());
    }
	
	@Test
    void testBookToString() {
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "67890");

        String result = book.toString();
        assertTrue(result.contains("The Hobbit"));
        assertTrue(result.contains("J.R.R. Tolkien"));
        assertTrue(result.contains("67890"));
    }
	@Test
    void testGetFinePerDay() {
        Book book = new Book("1984", "George Orwell", "12345");
        assertEquals(10, book.getFinePerDay(),
            "Fine per day should be 10");
    }

    @Test
    void testGetLoanPeriodDays() {
        Book book = new Book("1984", "George Orwell", "12345");
        assertEquals(28, book.getLoanPeriodDays(),
            "Loan period should be 28 days");
    }

	
}
