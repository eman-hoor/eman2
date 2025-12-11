
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
/**
 * Service class that manages library members.
 * Provides functionality to add, find, unregister, pay fines,
 * and persist members to and from files.
 *
 * @author hoor
 * @version 1.0
 */
public class MemberService {

    /** Map of members keyed by their unique ID. */
	private Map<String, Member> members = new HashMap<>();
	/**
     * Adds a new member to the collection.
     *
     * @param member the member to add
     * @throws IllegalArgumentException if a member with the same ID already exists
     */
    public void addMember(Member member) {
        if (members.containsKey(member.getMemberId())) {
            throw new IllegalArgumentException("Member with ID already exists.");
        }
        members.put(member.getMemberId(), member);
    }
    /**
     * Finds a member by their ID.
     *
     * @param id the member ID
     * @return the member object, or {@code null} if not found
     */
    public Member findById(String id) {
        return members.get(id);
    }
    /**
     * Finds members by their name (case-insensitive).
     *
     * @param name the member name
     * @return list of members with the given name
     */

    public List<Member> findByName(String name) {
        return members.values().stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .toList();
    }
    /**
     * Gets all members in the collection.
     *
     * @return list of all members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
	 // for sprint 4
    /**
     * Unregisters a member from the system.
     * A member cannot be unregistered if they have borrowed books
     * or have unpaid fines.
     *
     * @param memberId    the ID of the member to unregister
     * @param bookService the book service used to check borrowed books
     * @throws IllegalArgumentException if the member is not found
     * @throws IllegalStateException    if the member has borrowed books or unpaid fines
     */
    public void unregisterMember(String memberId, BookService bookService) {
        Member member = members.get(memberId);
        if (member == null) throw new IllegalArgumentException("Member not found");

        boolean hasBorrowed = bookService.getAllBooks().stream()
                .anyMatch(b -> memberId.equals(b.getBorrowedBy())); // refine to check borrowed by this member
        if (hasBorrowed || member.getFineBalance() > 0) {
            throw new IllegalStateException("Cannot unregister: outstanding books or fines.");
        }
        members.remove(memberId); 
    }
    //======================================
    /**
     * Pays part or all of a member's fine balance.
     * Fine balance will not go below zero.
     *
     * @param memberId the ID of the member paying the fine
     * @param amount   the amount to pay
     */
    public void payFine(String memberId, double amount) {
        Member m = members.get(memberId);
        if (m != null) {
            m.setfineBalance(Math.max(0, m.getFineBalance() - amount));
        }
    } 
    
 // ✅ File I/O
    //read
    /**
     * Loads members from a file.
     * Each line should be in the format:
     * memberId,name,contact,fineBalance
     *
     * @param filename the file to load from
     */
    public void loadMembers(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Member m = new Member(parts[0], parts[1], parts[2]);
                    m.setfineBalance(Double.parseDouble(parts[3]));
                    members.put(m.getMemberId(), m);
                }
            }
        } catch (IOException e) {
            System.out.println("No members file found, starting fresh.");
        }
    }
//write
    /**
     * Saves all members to a file.
     * Each line is written in the format:
     * memberId,name,contact,fineBalance
     *
     * @param filename the file to save to
     */
    public void saveMembers(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Member m : members.values()) {
                pw.println(m.getMemberId() + "," + m.getName() + "," +
                           m.getContact() + "," + m.getFineBalance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
	
}
