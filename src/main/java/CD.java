import java.time.LocalDate;
/**
 * Represents a CD in the library system.
 * Each CD has an ID, title, artist, and borrowing status.
 * Provides methods to borrow, return, and serialize/deserialize for file storage.
 *
 * @author hoor
 * @version 1.0
 */
public class CD {
    /** The unique identifier of the CD. */
	private String id;
    /** The title of the CD. */
    private String title;
    /** The artist of the CD. */
    private String artist;
    /** Whether the CD is currently borrowed. */
    private boolean borrowed;
    /** The ID of the member who borrowed the CD. */
    private String borrowedBy;
    /** The due date for returning the CD, if borrowed. */
    private LocalDate dueDate;
    /**
     * Constructs a new CD with the given details.
     *
     * @param id     the unique identifier of the CD
     * @param title  the title of the CD
     * @param artist the artist of the CD
     */

    public CD(String id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.borrowed = false;
    }
    /**
     * Marks the CD as borrowed by a member.
     * Sets the due date to 7 days from today.
     *
     * @param memberId the ID of the member borrowing the CD
     */

    public void borrow(String memberId) {
        this.borrowed = true; 
        this.borrowedBy = memberId;
        this.dueDate = LocalDate.now().plusDays(7); //  7-day loan
    }

    /**
     * Marks the CD as returned.
     * Resets borrowed status, due date, and borrower ID.
     */
    public void returnCD() {
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueDate = null;
    }
 // Getters
    /**
     * Gets the CD's unique identifier.
     *
     * @return the CD ID
     */
    public String getId() { return id; }
    /**
     * Gets the CD's title.
     *
     * @return the CD title
     */
    public String getTitle() { return title; }
    /**
     * Gets the CD's artist.
     *
     * @return the CD artist
     */
    public String getArtist() { return artist; }
    /**
     * Checks if the CD is currently borrowed.
     *
     * @return {@code true} if borrowed, {@code false} otherwise
     */
    public boolean isBorrowed() { return borrowed; }
    /**
     * Gets the ID of the member who borrowed the CD.
     *
     * @return the member ID, or {@code null} if not borrowed
     */
    public String getBorrowedBy() { return borrowedBy; }
    /**
     * Gets the due date for returning the CD.
     *
     * @return the due date, or {@code null} if not borrowed
     */
    public LocalDate getDueDate() { return dueDate; }
    /**
     * Returns a string representation of the CD suitable for saving to a file.
     * Format: id,title,artist,status
     * where status is either "available" or "borrowed:memberId".
     *
     * @return formatted string with CD details
     */
 // For saving to file
    @Override
    public String toString() {
        if (borrowed) {
            return id + "," + title + "," + artist + ",borrowed:" + borrowedBy;
        } else {
            return id + "," + title + "," + artist + ",available";
        }
    }
    /**
     * Creates a CD object from a string record.
     * Expected format: id,title,artist,status
     * where status is either "available" or "borrowed:memberId".
     *
     * @param line the string record
     * @return a CD object parsed from the string
     * @throws IllegalArgumentException if the record format is invalid
     */
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
        return cd;  //testttttttttt thiiis  
    }
    /**
     * Sets a custom due date for the CD.
     *
     * @param dueDate the new due date
     */
 
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    } 
    /**
     * Sets the ID of the member who borrowed the CD.
     * Useful for testing or manual overrides.
     *
     * @param memberId the member ID
     */

    // Optionally, add a setter for borrowedBy if needed in tests:
    public void setBorrowedBy(String memberId) {
        this.borrowedBy = memberId;
    }

	
	
}
