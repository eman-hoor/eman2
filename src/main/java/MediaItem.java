import java.time.LocalDate;

public interface MediaItem {
    String getId();
    String getTitle();
    boolean isBorrowed();
    String getAuthor();
    void setBorrowed(boolean borrowed);
    void borrow(String memberId);
    void returnBook();
    LocalDate getDueDate();
    void setDueDate(LocalDate dueDate);
    String getBorrowedBy();
    int getLoanPeriodDays();   
    int getFinePerDay();       
}
