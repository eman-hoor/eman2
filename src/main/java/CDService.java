import java.util.*;
import java.io.*;
/**
 * Service class that manages CDs in the library system.
 * Provides functionality to add, find, borrow, return,
 * and persist CDs to and from files.
 *
 * @author hoor
 * @version 1.0
 */
public class CDService {

	 /**
     * Map of CDs keyed by their unique ID.
     */
	private Map<String, CD> cds = new HashMap<>();
	/**
     * Adds a new CD to the collection.
     *
     * @param cd the CD to add
     */

    public void addCD(CD cd) {
        cds.put(cd.getId(), cd);
    }
    /**
     * Finds a CD by its ID.
     *
     * @param id the CD ID
     * @return the CD object, or {@code null} if not found
     */

    public CD findById(String id) {
        return cds.get(id);
    }
    /**
     * Allows a member to borrow a CD.
     * The loan period is handled inside the {@link CD} class (7 days).
     *
     * @param member the member borrowing the CD
     * @param cdId   the ID of the CD to borrow
     * @throws IllegalArgumentException if the CD is not found
     * @throws IllegalStateException    if the CD is already borrowed
     *                                  or the member has unpaid fines
     */

    public void borrowCD(Member member, String cdId) {
        CD cd = cds.get(cdId);
        if (cd == null) throw new IllegalArgumentException("CD not found.");
        if (cd.isBorrowed()) throw new IllegalStateException("CD already borrowed.");
        if (member.getFineBalance() > 0) throw new IllegalStateException("Member has unpaid fines.");
        cd.borrow(member.getMemberId()); //  7-day loan handled inside CD class
    }
    /**
     * Allows a member to return a borrowed CD.
     *
     * @param member the member returning the CD
     * @param cdId   the ID of the CD to return
     * @throws IllegalArgumentException if the CD is not found
     * @throws IllegalStateException    if the CD is not currently borrowed
     */

    public void returnCD(Member member, String cdId) {
        CD cd = cds.get(cdId);
        if (cd == null) throw new IllegalArgumentException("CD not found.");
        if (!cd.isBorrowed()) throw new IllegalStateException("CD is not currently borrowed.");
        cd.returnCD();
    }
    /**
     * Gets all CDs in the collection.
     *
     * @return a collection of all CDs
     */

    public Collection<CD> getAllCDs() {
        return cds.values();  
        //return new ArrayList<>(cds.values());   

    }
    /**
     * Saves all CDs to a file.
     * Each line is written in the format:
     * id,title,artist,status
     * where status is either "available" or "borrowed:memberId".
     *
     * @param filename the file to save to
     * @throws IOException if an I/O error occurs while writing
     */
	
 // Save to cds.txt
    public void saveCDs(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (CD cd : cds.values()) {
                writer.println(cd.toString());
            }
        }
    }
    /**
     * Loads CDs from a file.
     * Each line should be in the format:
     * id,title,artist,status
     * where status is either "available" or "borrowed:memberId".
     *
     * @param filename the file to load from
     * @throws IOException if an I/O error occurs while reading
     */
 //  Load from cds.txt
    public void loadCDs(String filename) throws IOException {
        cds.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                CD cd = CD.fromString(line);
                cds.put(cd.getId(), cd);
            }
        }
    }
	
	
	
}
