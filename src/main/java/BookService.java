import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Service class that manages library books.
 * Provides functionality to add, search, borrow, return,
 * and persist books to and from files.
 *
 * @author hoor
 * @version 1.0
 */
public class BookService {
	/**
     * Map of books keyed by their ISBN (lowercased).
     */
    private Map<String, Book> books = new HashMap<>();
    /**
     * Adds a new book to the collection.
     *
     * @param book the book to add
     * @throws IllegalArgumentException if a book with the same ISBN already exists
     */

    public void addBook(Book book) {
        
    	String isbnKey = book.getIsbn().toLowerCase();
        if (books.containsKey(isbnKey)) {
            throw new IllegalArgumentException("Book with ISBN already exists.");
        }
        books.put(isbnKey, book);
      }
    /**
     * Searches for books by title, author, or ISBN.
     *
     * @param query the search query string
     * @return list of books matching the query
     */
 
    public List<Book> searchBook(String query) {
        String q = query.toLowerCase().trim();
        return books.values().stream()
            .filter(b -> b.getTitle().toLowerCase().contains(q)
                      || b.getAuthor().toLowerCase().contains(q)
                      || b.getIsbn().toLowerCase().contains(q))
            .toList();
    }
    /**
     * Gets all books in the collection.
     *
     * @return list of all books
     */
 
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());   
        }
   //----------------------------------------
    /**
     * Allows a member to borrow a book.
     * Sets the due date to 28 days from today.
     *
     * @param member the member borrowing the book
     * @param isbn   the ISBN of the book to borrow
     * @throws IllegalArgumentException if the book is not found
     * @throws IllegalStateException    if the book is already borrowed,
     *                                  the member has unpaid fines,
     *                                  or the member has overdue books
     */
    public void borrowBook(Member member, String isbn) {
    	Book b = books.get(isbn);
        if (b == null) throw new IllegalArgumentException("Book not found.");
        if (b.isBorrowed()) throw new IllegalStateException("Book already borrowed.");
        if (member.getFineBalance() > 0) throw new IllegalStateException("Member has unpaid fines.");
        // Could also check overdue books here for Sprint 4
        for (Book b2 : getAllBooks()) {
            if (b2.isBorrowed() 
                && b2.getBorrowedBy().equals(member.getMemberId()) 
                && b2.getDueDate() != null 
                && b2.getDueDate().isBefore(LocalDate.now())) {
                throw new IllegalStateException("Member has overdue books.");
            }
        }
        b.borrow(member.getMemberId());
        b.setDueDate(LocalDate.now().plusDays(28));
    }
    /**
     * Allows a member to return a borrowed book.
     *
     * @param member the member returning the book
     * @param isbn   the ISBN of the book to return
     * @throws IllegalArgumentException if the book is not found
     * @throws IllegalStateException    if the book is not currently borrowed
     */
 
    
    public void returnBook(Member member, String isbn) {
    	Book b = books.get(isbn);
        if (b == null) throw new IllegalArgumentException("Book not found.");
        if (!b.isBorrowed()) throw new IllegalStateException("Book is not borrowed.");
        b.returnBook();
    
    }
    /**
     * Finds a book by its ISBN.
     *
     * @param isbn the ISBN of the book
     * @return the book, or {@code null} if not found
     */
    
    public Book findBook(String isbn) {
        return books.get(isbn.toLowerCase());    }
    
    //========================
    /**
     * Loads books from a file.
     * Each line should be in the format:
     * ISBN,Title,Author,Status
     * where Status is either "available" or "borrowed:memberId".
     *
     * @param filename the file to load from
     */
 //  File I/O
    public void loadBooks(String filename) {
    	 try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
             String line;
             while ((line = br.readLine()) != null) {
                 String[] parts = line.split(",");
                 if (parts.length >= 4) {
                     Book b = new Book(parts[1], parts[2], parts[0]);
                     if (parts[3].startsWith("borrowed:")) {
                         b.borrow(parts[3].substring(9)); // memberId
                     }
                     books.put(b.getIsbn(), b);
                 }
             }
         } catch (IOException e) {
             System.out.println("No books file found, starting fresh.");
         }
    }
    /**
     * Saves all books to a file.
     * Each line is written in the format:
     * ISBN,Title,Author,Status
     * where Status is either "available" or "borrowed:memberId".
     *
     * @param filename the file to save to
     */

    public void saveBooks(String filename) {
    	try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Book b : books.values()) {
                String status = b.isBorrowed() ? "borrowed:" + b.getBorrowedBy() : "available";
                pw.println(b.getIsbn() + "," + b.getTitle() + "," + b.getAuthor() + "," + status);
            }
        } catch (IOException e) {
            System.out.println("Failed to save books: " + e.getMessage());
        }
    }
    }
    
    

