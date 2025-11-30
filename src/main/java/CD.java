import java.time.LocalDate;

public class CD {
	
	private String id;
    private String title;
    private String artist;
    private boolean borrowed;
    private String borrowedBy;
    private LocalDate dueDate;

    public CD(String id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.borrowed = false;
    }

    public void borrow(String memberId) {
        this.borrowed = true; 
        this.borrowedBy = memberId;
        this.dueDate = LocalDate.now().plusDays(7); // ✅ 7-day loan
    }

    public void returnCD() {
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueDate = null;
    }

    public boolean isBorrowed() { return borrowed; }
    public String getBorrowedBy() { return borrowedBy; }
    public LocalDate getDueDate() { return dueDate; }
	
	
	
}
