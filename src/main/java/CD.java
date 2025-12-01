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
        this.dueDate = LocalDate.now().plusDays(7); //  7-day loan
    }

    public void returnCD() {
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueDate = null;
    }
 // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
   
    public boolean isBorrowed() { return borrowed; }
    public String getBorrowedBy() { return borrowedBy; }
    public LocalDate getDueDate() { return dueDate; }
	
 // For saving to file
    @Override
    public String toString() {
        if (borrowed) {
            return id + "," + title + "," + artist + ",borrowed:" + borrowedBy;
        } else {
            return id + "," + title + "," + artist + ",available";
        }
    }
    
 // For loading from file
    public static CD fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) throw new IllegalArgumentException("Invalid CD record: " + line);

        CD cd = new CD(parts[0], parts[1], parts[2]);
        if (parts[3].startsWith("borrowed:")) {
            cd.borrowed = true;
            cd.borrowedBy = parts[3].substring("borrowed:".length());
            cd.dueDate = LocalDate.now().plusDays(7); // approximate reload
        }
        return cd;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Optionally, add a setter for borrowedBy if needed in tests:
    public void setBorrowedBy(String memberId) {
        this.borrowedBy = memberId;
    }

	
	
}
