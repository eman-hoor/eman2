import java.time.LocalDate;

public class Book {

	private String title;
    private String author;
    private String isbn;
    private boolean borrowed = false;
    private LocalDate dueDate;

    
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn; 
    }

    public String getTitle() { 
    	return title;
    	}
    public String getAuthor() { 
    	return author;
    	}
    public String getIsbn() {  
    	return isbn; 
    	}

    /*@Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ")";
    }*/
	
    @Override
    public String toString() {
        return isbn + " - " + title + " by " + author +
               (borrowed ? " (Borrowed by " + borrowedBy + ", due " + dueDate + ")" : " (Available)");
    }
    
    public boolean isBorrowed() { 
    	
    	return borrowed;
    }
    
    public LocalDate getDueDate() { 
    	
    	return dueDate;
    }
    
    private String borrowedBy; // memberId of borrower
    
    public void borrow(String memberId) {
        borrowed = true;
        borrowedBy = memberId;
        dueDate = LocalDate.now().plusDays(28);
    }

    public void returnBook() {
        borrowed = false;
        dueDate = null; 
        borrowedBy = null;
    }
    
    public String getBorrowedBy() { 
    	return borrowedBy;
    	}
    
    
 // For testing: allow manual due date adjustment
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    
    
    
}
