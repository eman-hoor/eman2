import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookServiceTest {

    private BookService service;
    private Path tempFile;
	
	@BeforeEach
	void setUp() throws Exception {
        service = new BookService();
        
        service.addBook(new Book("1984", "George Orwell", "12345"));
        service.addBook(new Book("The Hobbit", "J.R.R. Tolkien", "67890"));
        tempFile = Files.createTempFile("books", ".txt");
	}

	@AfterEach
	void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
	}

	
	
	@Test
	void testAddAndSearchBook() {
		
        List<Book> results = service.searchBook("1984");
        assertEquals(1, results.size());
        assertEquals("George Orwell", results.get(0).getAuthor());
		
	}
	
	
	@Test
    void testSearchByAuthor() {
        List<Book> results = service.searchBook("J.R.R. Tolkien");
        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).getTitle());
    }
	
	@Test
	void testSearchNoResults() {
	    assertTrue(service.searchBook("Nonexistent").isEmpty());
	}
	
	@Test
	void testSearchByIsbn() {
	    assertEquals(1, service.searchBook("12345").size());
	}
	
	@Test
    void testAddBookAndGetAllBooks() {

        List<Book> allBooks = service.getAllBooks();
        assertEquals(2, allBooks.size());
        assertEquals("1984", allBooks.get(0).getTitle());
    }
	
	@Test
    void testSearchByTitle() {
        List<Book> results = service.searchBook("1984");

        assertEquals(1, results.size());
        assertEquals("George Orwell", results.get(0).getAuthor());
    }
	
	@Test
    void testMultipleBooksSearch() {
        service.addBook(new Book("Animal Farm", "George Orwell", "67899"));
        List<Book> results = service.searchBook("George Orwell");
        assertEquals(2, results.size());
    }
	
	@Test
    void testPartialTitleSearch() {
        List<Book> results = service.searchBook("Hob");
        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).getTitle());
    }
	
	@Test
    void testPartialAuthorSearch() {
        List<Book> results = service.searchBook("Orwell");
        assertEquals(1, results.size());
        assertEquals("1984", results.get(0).getTitle());
    }
	
	@Test
    void testPartialIsbnSearch() {
        List<Book> results = service.searchBook("678");
        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).getTitle());
    }

    @Test
    void testNoResultsForPartialSearch() {
        List<Book> results = service.searchBook("Shakespeare");
        assertTrue(results.isEmpty());
    }

    @Test
    void testMultipleMatches() {
        service.addBook(new Book("Animal Farm", "George Orwell", "54321"));
        List<Book> results = service.searchBook("Orwell");
        assertEquals(2, results.size());
    }
	
    
    @Test
    void testAddDuplicateIsbnThrowsException() {
        Book book2 = new Book("Duplicate Title", "Another Author", "12345");

        Exception exception = assertThrows(IllegalArgumentException.class,
        		() ->  service.addBook(book2)
        );

        assertTrue(exception.getMessage().contains("already exists"));
    }
    
    @Test
    void testCaseInsensitiveIsbnCheck() {
        Book book1 = new Book("life", "amy sam", "ABC123");
        Book book2 = new Book("Another Book", "Another Author", "abc123");

        service.addBook(book1);

        Exception exception = assertThrows(IllegalArgumentException.class,
        		() ->  service.addBook(book2)
        );

        assertTrue(exception.getMessage().contains("already exists"));
    }
    
    @Test
    void testGetAllBooksWhenEmpty() {
        BookService emptyService = new BookService();
        assertTrue(emptyService.getAllBooks().isEmpty());
    }
     
    @Test
    void testDuplicateIsbnDoesNotAddBook() {
        int initialSize = service.getAllBooks().size();
        Book duplicate = new Book("Duplicate", "Author", "12345");

        assertThrows(IllegalArgumentException.class, () -> service.addBook(duplicate));
        assertEquals(initialSize, service.getAllBooks().size());
    }

    //------------------------------
    @Test
    void testSaveAndLoadBooks() {
        // Arrange: add books
        Book b1 = new Book("1988", "jon well", "12344");
        Book b2 = new Book("Hot", "mannly", "75840");

        service.addBook(b1);
        service.addBook(b2);

        // Act: save to file
        service.saveBooks(tempFile.toString());

        // Create a new service and load from file
        BookService loadedService = new BookService();
        loadedService.loadBooks(tempFile.toString());

        // Assert: books are correctly loaded
        List<Book> loadedBooks = loadedService.getAllBooks();
        assertEquals(4, loadedBooks.size());

        Book loadedB1 = loadedService.findBook("12344");
        assertNotNull(loadedB1);
        assertEquals("1988", loadedB1.getTitle());
        assertEquals("jon well", loadedB1.getAuthor());
        assertFalse(loadedB1.isBorrowed());

        Book loadedB2 = loadedService.findBook("75840");
        assertNotNull(loadedB2);
        assertEquals("Hot", loadedB2.getTitle());
        assertEquals("mannly", loadedB2.getAuthor());
        assertFalse(loadedB2.isBorrowed());
    }
    
    @Test
    void testBorrowedBookPersistence() {
        // Arrange: add and borrow a book
        Book b1 = new Book("1988", "jon well", "12344");
        service.addBook(b1);
        b1.borrow("M1"); // simulate borrowed by member M1

        // Act: save to file
        service.saveBooks(tempFile.toString());

        // Load into new service
        BookService loadedService = new BookService();
        loadedService.loadBooks(tempFile.toString());

        // Assert: borrowed status persisted
        Book loadedB1 = loadedService.findBook("12344");
        assertNotNull(loadedB1);
        assertTrue(loadedB1.isBorrowed());
        assertEquals("M1", loadedB1.getBorrowedBy());
    }
    
    @Test
    void testLoadBooksHandlesMissingFile() {
        BookService service = new BookService();

        // Use a filename that doesn't exist
        service.loadBooks("nonexistent_file.csv");

        // Assert that no books were loaded
        assertTrue(service.getAllBooks().isEmpty(),
            "BookService should start fresh when file is missing");
    }
    
    @Test
    void testSaveBooksHandlesIOException() {
        BookService service = new BookService();

        // Add a book so saveBooks tries to write something
        Book b = new Book("1984", "George Orwell", "12345");
        service.addBook(b);

        // Try to save to a directory instead of a file
        service.saveBooks("/");  // On most systems this will fail

        // No exception should escape, even though writing failed
        assertFalse(service.getAllBooks().isEmpty(),
            "Books should remain in memory even if saving fails");
    }
    
    
    
}
