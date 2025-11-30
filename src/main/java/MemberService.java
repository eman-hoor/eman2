
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
public class MemberService {

	
	private Map<String, Member> members = new HashMap<>();

    public void addMember(Member member) {
        if (members.containsKey(member.getMemberId())) {
            throw new IllegalArgumentException("Member with ID already exists.");
        }
        members.put(member.getMemberId(), member);
    }

    public Member findById(String id) {
        return members.get(id);
    }

    public List<Member> findByName(String name) {
        return members.values().stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .toList();
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
	 // for sprint 4
    
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
    public void payFine(String memberId, double amount) {
        Member m = members.get(memberId);
        if (m != null) {
            m.setfineBalance(Math.max(0, m.getFineBalance() - amount));
        }
    } 
    
 // ✅ File I/O
    //read
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
