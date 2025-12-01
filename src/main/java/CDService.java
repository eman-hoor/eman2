import java.util.*;
import java.io.*;
public class CDService {

	private Map<String, CD> cds = new HashMap<>();

    public void addCD(CD cd) {
        cds.put(cd.getId(), cd);
    }

    public CD findById(String id) {
        return cds.get(id);
    }

    public void borrowCD(Member member, String cdId) {
        CD cd = cds.get(cdId);
        if (cd == null) throw new IllegalArgumentException("CD not found.");
        if (cd.isBorrowed()) throw new IllegalStateException("CD already borrowed.");
        if (member.getFineBalance() > 0) throw new IllegalStateException("Member has unpaid fines.");
        cd.borrow(member.getMemberId()); //  7-day loan handled inside CD class
    }

    public void returnCD(Member member, String cdId) {
        CD cd = cds.get(cdId);
        if (cd == null) throw new IllegalArgumentException("CD not found.");
        if (!cd.isBorrowed()) throw new IllegalStateException("CD is not currently borrowed.");
        cd.returnCD();
    }

    public Collection<CD> getAllCDs() {
        return cds.values();  
        //return new ArrayList<>(cds.values());   


    }
	
 // Save to cds.txt
    public void saveCDs(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (CD cd : cds.values()) {
                writer.println(cd.toString());
            }
        }
    }
	 
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
