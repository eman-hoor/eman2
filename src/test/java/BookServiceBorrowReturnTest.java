import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookServiceBorrowReturnTest {

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
    void testBorrowAndReturnBook() {
        BookService service = new BookService();
        Member member = new Member("M1", "Alice", "a@mail.com");
        Book book = new Book("1984", "George Orwell", "12345");
        service.addBook(book);

        service.borrowBook(member, "12345");
        assertTrue(book.isBorrowed());
        assertEquals(LocalDate.now().plusDays(28), book.getDueDate());

        service.returnBook(member, "12345");
        assertFalse(book.isBorrowed());
    }
	
	@Test
    void testBorrowAlreadyBorrowedThrows() {
        BookService service = new BookService();
        Member member = new Member("M1", "Alice", "a@mail.com");
        Book book = new Book("1984", "George Orwell", "12345");
        service.addBook(book);

        service.borrowBook(member, "12345");
        assertThrows(IllegalStateException.class, () -> service.borrowBook(member, "12345"));
    }
	
	
	
	
	
	

}
