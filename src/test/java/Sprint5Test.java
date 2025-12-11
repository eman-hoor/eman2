import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class Sprint5Test {

	
	@Test
	void testBorrowCDSetsDueDate() {
	    Member member = new Member("M1", "Alice", "alice@example.com");
	    CD cd = new CD("C1", "Thriller", "Michael Jackson");

	    cd.borrow(member.getMemberId());

	    assertTrue(cd.isBorrowed()); 
	    assertEquals("M1", cd.getBorrowedBy()); 
	    assertEquals(LocalDate.now().plusDays(7), cd.getDueDate());
	}

	
	@Test
	void testCDFineStrategy() {
	    FineStrategy strategy = new CDFineStrategy();
	    int fine = strategy.calculateFine(3); // 3 days overdue
	    assertEquals(60, fine); // 3 * 20
	}

	@Test
	void testBookFineStrategy() {
	    FineStrategy strategy = new BookFineStrategy();
	    int fine = strategy.calculateFine(2); // 2 days overdue
	    assertEquals(20, fine); // 2 * 10
	}

	@Test
	void testPayFineReducesBalance() {
	    Member member = new Member("M2", "Bob", "bob@example.com");
	    member.addFine(50); // assume addFine method exists
	    member.payFine(20);

	    assertEquals(30, member.getFineBalance());
	}
	private CDService cdService;
    private Member member;
    
	@Test
    void testAddAndFindCD() {
		cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        CD cd = new CD("C1", "Thriller", "Michael Jackson");
        cdService.addCD(cd);

        CD found = cdService.findById("C1");
        assertNotNull(found);
        assertEquals("Thriller", found.getTitle());
    }

    @Test
    void testBorrowCDSuccess() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        CD cd = new CD("C2", "Back in Black", "AC/DC");
        cdService.addCD(cd);

        cdService.borrowCD(member, "C2");

        CD borrowed = cdService.findById("C2");
        assertTrue(borrowed.isBorrowed());
        assertEquals("M1", borrowed.getBorrowedBy());
        assertEquals(LocalDate.now().plusDays(7), borrowed.getDueDate());
    }

    @Test
    void testBorrowCDFailsIfNotFound() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        assertThrows(IllegalArgumentException.class,
            () -> cdService.borrowCD(member, "X999"));
    }

    @Test
    void testBorrowCDFailsIfAlreadyBorrowed() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        CD cd = new CD("C3", "Hybrid Theory", "Linkin Park");
        cdService.addCD(cd);
        cdService.borrowCD(member, "C3");

        assertThrows(IllegalStateException.class,
            () -> cdService.borrowCD(member, "C3"));
    }

    @Test
    void testBorrowCDFailsIfMemberHasUnpaidFines() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        member.addFine(50); // assume addFine exists
        CD cd = new CD("C4", "Nevermind", "Nirvana");
        cdService.addCD(cd);

        assertThrows(IllegalStateException.class,
            () -> cdService.borrowCD(member, "C4"));
    }

    @Test
    void testReturnCDSuccess() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        CD cd = new CD("C5", "Abbey Road", "The Beatles");
        cdService.addCD(cd);
        cdService.borrowCD(member, "C5");

        cdService.returnCD(member, "C5");

        CD returned = cdService.findById("C5");
        assertFalse(returned.isBorrowed());
        assertNull(returned.getBorrowedBy());
    }

    @Test
    void testReturnCDFailsIfNotFound() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        assertThrows(IllegalArgumentException.class,
            () -> cdService.returnCD(member, "X123"));
    }

    @Test
    void testReturnCDFailsIfNotBorrowed() {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        CD cd = new CD("C6", "Dark Side of the Moon", "Pink Floyd");
        cdService.addCD(cd);

        assertThrows(IllegalStateException.class,
            () -> cdService.returnCD(member, "C6"));
    }

    @Test
    void testSaveAndLoadCDs() throws IOException {
    	cdService = new CDService();
        member = new Member("M1", "Alice", "alice@example.com");
        CD cd = new CD("C7", "OK Computer", "Radiohead");
        cdService.addCD(cd);

        String filename = "test_cds.txt";
        cdService.saveCDs(filename);

        CDService newService = new CDService();
        newService.loadCDs(filename);

        CD loaded = newService.findById("C7");
        assertNotNull(loaded);
        assertEquals("OK Computer", loaded.getTitle());

        // cleanup
        new File(filename).delete();
    }
	
	
    @Test
    void testFromStringAvailable() {
        String line = "1,Thriller,Michael Jackson,available";
        CD cd = CD.fromString(line);

        assertEquals("1", cd.getId());
        assertEquals("Thriller", cd.getTitle());
        assertEquals("Michael Jackson", cd.getArtist());
        assertFalse(cd.isBorrowed());
        assertNull(cd.getBorrowedBy());
        assertNull(cd.getDueDate());
    }
    
    @Test
    void testFromStringBorrowed() {
        String line = "2,Back in Black,ACDC,borrowed:M1";
        CD cd = CD.fromString(line);

        assertEquals("2", cd.getId());
        assertEquals("Back in Black", cd.getTitle());
        assertEquals("ACDC", cd.getArtist());
        assertTrue(cd.isBorrowed());
        assertEquals("M1", cd.getBorrowedBy());
        assertNotNull(cd.getDueDate()); // dueDate set when borrowed
    }
    
    @Test
    void testToStringAvailable() {
        CD cd = new CD("3", "Rumours", "Fleetwood Mac");
        String result = cd.toString();
        assertEquals("3,Rumours,Fleetwood Mac,available", result);
    }

    @Test
    void testToStringBorrowed() {
        CD cd = new CD("4", "Abbey Road", "The Beatles");
        cd.borrow("M2");
        String result = cd.toString();
        assertEquals("4,Abbey Road,The Beatles,borrowed:M2", result);
    }

    @Test
    void testSetBorrowedBy() {
        CD cd = new CD("5", "Hotel California", "Eagles");
        cd.setBorrowedBy("M3");
        assertEquals("M3", cd.getBorrowedBy());
    }
    @Test
    void testGetArtist() {
        CD cd = new CD("6", "Dark Side of the Moon", "Pink Floyd");
        assertEquals("Pink Floyd", cd.getArtist());
    }
	
	//********************************************fine strat
    @Test
    void testBookFineStrategy2() {
        FineStrategy bookStrategy = new BookFineStrategy();
        Loan loan = new Loan(bookStrategy, 3); // 3 days overdue
        assertEquals(30, loan.calculateFine(),
            "Book fine should be 3 * 10 = 30");
    }

    @Test
    void testCDFineStrategy2() {
        FineStrategy cdStrategy = new CDFineStrategy();
        Loan loan = new Loan(cdStrategy, 2); // 2 days overdue
        assertEquals(40, loan.calculateFine(),
            "CD fine should be 2 * 20 = 40");
    }

    @Test
    void testZeroOverdueDays() {
        FineStrategy bookStrategy = new BookFineStrategy();
        Loan loan = new Loan(bookStrategy, 0); // no overdue
        assertEquals(0, loan.calculateFine(),
            "Fine should be 0 when overdueDays = 0");
    }

    @Test
    void testDifferentStrategiesGiveDifferentResults() {
        Loan bookLoan = new Loan(new BookFineStrategy(), 5); // 5 days overdue
        Loan cdLoan = new Loan(new CDFineStrategy(), 5);

        assertEquals(50, bookLoan.calculateFine(),
            "Book fine should be 5 * 10 = 50");
        assertEquals(100, cdLoan.calculateFine(),
            "CD fine should be 5 * 20 = 100");
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
   /* @Test
    public void testBookAndCDHaveDifferentRules() {
        Book book = new Book("Java", "Oracle", "B123");
        CD cd = new CD("Thriller", "MJ", "CD999");

        assertEquals(28, book.getLoanPeriodDays());
        assertEquals(10, book.getFinePerDay());

        assertEquals(7, cd.getLoanPeriodDays());
        assertEquals(20, cd.getFinePerDay());
    }

    @Test
    public void testFineCalculation_Book5DaysLate() {
        Book book = new Book("Test", "Author", "B1");
        book.borrow("M1");
        book.setDueDate(LocalDate.now().minusDays(5));

        int fine = FineCalculator.calculateFine(book, LocalDate.now());
        assertEquals(50, fine); // 5 × 10
    }

    @Test
    public void testFineCalculation_CD3DaysLate() {
        CD cd = new CD("Test CD", "Artist", "C1");
        cd.borrow("M1");
        cd.setDueDate(LocalDate.now().minusDays(3));

        int fine = FineCalculator.calculateFine(cd, LocalDate.now());
        assertEquals(60, fine); // 3 × 20
    }

    @Test
    public void testMixedFineCalculation() {
        Book book = new Book("B", "A", "1");
        CD cd = new CD("C", "A", "2");
        book.borrow("M1");
        cd.borrow("M1");
        book.setDueDate(LocalDate.now().minusDays(4));
        cd.setDueDate(LocalDate.now().minusDays(2));

        int total = FineCalculator.calculateFine(book, LocalDate.now()) +
                    FineCalculator.calculateFine(cd, LocalDate.now());

        assertEquals(80, total); // 4×10 + 2×20 = 80
    }*/
}