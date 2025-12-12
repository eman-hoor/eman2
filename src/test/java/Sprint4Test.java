import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Sprint4Test { 

	
	
	private BookService bookService;
    private MemberService memberService;
    private Member member;
    private Book book;
    


	@BeforeEach
	void setUp() throws Exception {
		
		bookService = new BookService();
        memberService = new MemberService();
        member = new Member("M1", "Alice", "alice@mail.com");
        memberService.addMember(member);
        book = new Book("1988", "George Orwell", "123456");
        bookService.addBook(book);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	// ---------------- Borrow Restrictions ----------------

    @Test
    void testBorrowSucceedsWhenNoFinesOrOverdue() {
        bookService.borrowBook(member, "123456");
        assertTrue(book.isBorrowed());
        assertEquals("M1", book.getBorrowedBy());
    }

    @Test
    void testBorrowFailsWhenMemberHasUnpaidFine() {
        member.addFine(10.0);
        assertThrows(IllegalStateException.class,
            () -> bookService.borrowBook(member, "123456"));
    }

/* @Test
    void testBorrowFailsWhenMemberHasOverdueBook() {
        // Borrow once
        bookService.borrowBook(member, "123456");
        // Manually set due date in the past to simulate overdue
        book.setDueDate(LocalDate.now().minusDays(5)); 

        // Try to borrow another book
        Book another = new Book("Brave New World", "Aldous Huxley", "67899");
        bookService.addBook(another);

        assertThrows(IllegalStateException.class,
            () -> bookService.borrowBook(member, "67899"));
    } 
    */
    @Test
    void testBorrowFailsWhenMemberHasOverdueBook() {
        bookService.borrowBook(member, "123456");
        book.setDueDate(LocalDate.now().minusDays(5)); // متأخر

        Book another = new Book("Brave New World", "Aldous Huxley", "67899");
        bookService.addBook(another);

    
        assertThrows(IllegalStateException.class,
            () -> bookService.borrowBook(member, "67899"));
    }

    @Test
    void testBorrowFailsWhenBookAlreadyBorrowed() {
        bookService.borrowBook(member, "123456");
        Member other = new Member("M2", "Bob", "bob@mail.com");
        memberService.addMember(other);

        assertThrows(IllegalStateException.class,
            () -> bookService.borrowBook(other, "123456"));
    }
    
 // ---------------- Unregister Member ----------------

    @Test
    void testUnregisterSucceedsWhenNoBooksAndNoFines() {
        memberService.unregisterMember("M1", bookService);
        assertNull(memberService.findById("M1"));
    }

    @Test
    void testUnregisterFailsWhenMemberHasBorrowedBooks() {
        bookService.borrowBook(member, "123456");
        assertThrows(IllegalStateException.class,
            () -> memberService.unregisterMember("M1", bookService));
    }

    @Test
    void testUnregisterFailsWhenMemberHasUnpaidFines() {
        member.addFine(5.0);
        assertThrows(IllegalStateException.class,
            () -> memberService.unregisterMember("M1", bookService));
    }

    @Test
    void testUnregisterFailsWhenMemberNotFound() {
        assertThrows(IllegalArgumentException.class,
            () -> memberService.unregisterMember("Unknown", bookService));
    }


}
