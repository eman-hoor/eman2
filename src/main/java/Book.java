
import java.time.LocalDate;
/**
 * Represents a book in the library system.
 * Each book has a title, author, ISBN, and borrowing status.
 * Provides methods to borrow, return, and calculate fines.
 *
 * @author hoor
 * @version 1.0
 */
public class Book {
    /** The title of the book. */
    private String title;
    /** The author of the book. */
    private String author;
    /** The ISBN identifier of the book. */
    private String isbn;
    /** Whether the book is currently borrowed. */
    private boolean borrowed = false;
    /** The due date for returning the book, if borrowed. */
    private LocalDate dueDate;
    /** The ID of the member who borrowed the book. */
    private String borrowedBy;
    
    /**
     * Constructs a new Book with the given details.
     *
     * @param title  the title of the book
     * @param author the author of the book
     * @param isbn   the ISBN of the book
     */

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn; 
    }
    /**
     * Gets the title of the book.
     *
     * @return the book title
     */

    public String getTitle() { return title; }
    /**
     * Gets the author of the book.
     *
     * @return the book author
     */
    public String getAuthor() { return author; }
    /**
     * Gets the ISBN of the book in lowercase.
     *
     * @return the ISBN string
     */
    public String getIsbn() { return isbn.toLowerCase(); }
    /**
     * Checks if the book is currently borrowed.
     *
     * @return {@code true} if borrowed, {@code false} otherwise
     */
    public boolean isBorrowed() { return borrowed; }
    /**
     * Gets the due date for returning the book.
     *
     * @return the due date, or {@code null} if not borrowed
     */
    public LocalDate getDueDate() { return dueDate; }
    /**
     * Gets the ID of the member who borrowed the book.
     *
     * @return the member ID, or {@code null} if not borrowed
     */
    public String getBorrowedBy() { return borrowedBy; }
    /**
     * Marks the book as borrowed by a member.
     * Sets the due date to 28 days from today.
     *
     * @param memberId the ID of the member borrowing the book
     */

    public void borrow(String memberId) {
        borrowed = true;
        borrowedBy = memberId;
        dueDate = LocalDate.now().plusDays(28);
    }
    /**
     * Marks the book as returned.
     * Resets borrowed status, due date, and borrower ID.
     */

    public void returnBook() {
        borrowed = false;
        dueDate = null; 
        borrowedBy = null;
    }
    /**
     * Sets a custom due date for the book.
     *
     * @param dueDate the new due date
     */

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    } 
    /**
     * Gets the fine charged per overdue day.
     *
     * @return fine amount per day (10 NIS)
     */

    public int getFinePerDay() {
        return 10;
    }
    /**
     * Gets the loan period in days.
     *
     * @return loan period (28 days)
     */

    public int getLoanPeriodDays() {
        return 28;
    }
    /**
     * Returns a string representation of the book.
     * Includes ISBN, title, author, and borrowing status.
     *
     * @return formatted string with book details
     */

    @Override
    public String toString() {
        return isbn + " - " + title + " by " + author +
               (borrowed ? " (Borrowed by " + borrowedBy + ", due " + dueDate + ")" : " (Available)");
    }
}