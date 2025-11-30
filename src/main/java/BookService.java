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
public class BookService {
	
	//private List<Book> books = new ArrayList<>();
    private Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        
    	String isbnKey = book.getIsbn().toLowerCase();
        if (books.containsKey(isbnKey)) {
            throw new IllegalArgumentException("Book with ISBN already exists.");
        }
        books.put(isbnKey, book);
      }

    public List<Book> searchBook(String query) {
        String q = query.toLowerCase().trim();
        return books.values().stream()
            .filter(b -> b.getTitle().toLowerCase().contains(q)
                      || b.getAuthor().toLowerCase().contains(q)
                      || b.getIsbn().toLowerCase().contains(q))
            .toList();
    }
 
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());   
        }
   //----------------------------------------
    
    public void borrowBook(Member member, String isbn) {
    	Book b = books.get(isbn);
        if (b == null) throw new IllegalArgumentException("Book not found.");
        if (b.isBorrowed()) throw new IllegalStateException("Book already borrowed.");
        if (member.getFineBalance() > 0) throw new IllegalStateException("Member has unpaid fines.");
        // Could also check overdue books here for Sprint 4
        b.borrow(member.getMemberId());
    }
 
    
    public void returnBook(Member member, String isbn) {
    	Book b = books.get(isbn);
        if (b == null) throw new IllegalArgumentException("Book not found.");
        if (!b.isBorrowed()) throw new IllegalStateException("Book is not borrowed.");
        b.returnBook();
    
    }
    
    public Book findBook(String isbn) {
        return books.get(isbn.toLowerCase());    }
    
    //========================
    
 // ✅ File I/O
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

    public void saveBooks(String filename) {
    	try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Book b : books.values()) {
                String status = b.isBorrowed() ? "borrowed:" + b.getBorrowedBy() : "available";
                pw.println(b.getIsbn() + "," + b.getTitle() + "," + b.getAuthor() + "," + status);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
    
    

