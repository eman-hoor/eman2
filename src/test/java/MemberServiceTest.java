import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberServiceTest {
    private MemberService service;
    private Path tempFile;


	@BeforeEach
	void setUp() throws Exception {
        service = new MemberService();
        tempFile = Files.createTempFile("members", ".txt");
	}

	@AfterEach
	void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
	}

	@Test
    void testAddAndFindMember() {
        MemberService service = new MemberService();
        Member m = new Member("M1", "Alice", "alice@mail.com");
        service.addMember(m);

        assertEquals("Alice", service.findById("M1").getName());
    }
	
	@Test
    void testFindByIdNotFoundReturnsNull() {
        assertNull(service.findById("Unknown"));
    }
	
	
	@Test
    void testDuplicateMemberThrows() {
        MemberService service = new MemberService();
        service.addMember(new Member("M1", "Alice", "a@mail.com"));
        assertThrows(IllegalArgumentException.class, () -> 
            service.addMember(new Member("M1", "Bob", "b@mail.com")));
    }
	
	@Test
    void testFindByNameSingleResult() {
        service.addMember(new Member("M1", "Alice", "a@mail.com"));
        List<Member> results = service.findByName("Alice");

        assertEquals(1, results.size());
        assertEquals("M1", results.get(0).getMemberId());
    }
	
	@Test
    void testFindByNameMultipleResults() {
        service.addMember(new Member("M1", "Alice", "a@mail.com"));
        service.addMember(new Member("M2", "Alice", "b@mail.com"));

        List<Member> results = service.findByName("Alice");
        assertEquals(2, results.size());
    }
	
	@Test
    void testFindByNameNoResults() {
        service.addMember(new Member("M1", "Alice", "a@mail.com"));
        List<Member> results = service.findByName("Bob");

        assertTrue(results.isEmpty());
    }
	
	
	@Test
    void testSaveAndLoadMembers() {
        // Arrange: add members
        Member m1 = new Member("M1", "Alice", "alice@example.com");
        m1.setfineBalance(10);
        Member m2 = new Member("M2", "Bob", "bob@example.com");
        m2.setfineBalance(0);

        service.addMember(m1);
        service.addMember(m2);

        // Act: save to file
        service.saveMembers(tempFile.toString());

        // Create a new service and load from file
        MemberService loadedService = new MemberService();
        loadedService.loadMembers(tempFile.toString());

        // Assert: members are correctly loaded
        List<Member> loadedMembers = loadedService.getAllMembers();
        assertEquals(2, loadedMembers.size());

        Member loadedM1 = loadedService.findById("M1");
        assertNotNull(loadedM1);
        assertEquals("Alice", loadedM1.getName());
        assertEquals(10, loadedM1.getFineBalance());

        Member loadedM2 = loadedService.findById("M2");
        assertNotNull(loadedM2);
        assertEquals("Bob", loadedM2.getName());
        assertEquals(0, loadedM2.getFineBalance());
    }
	
	@Test
    void testPayFineReducesBalance() {
        MemberService service = new MemberService();
        Member m = new Member("M1", "Alice", "alice@example.com");
        m.setfineBalance(50.0);
        service.addMember(m);

        service.payFine("M1", 20.0);

        assertEquals(30.0, service.findById("M1").getFineBalance(),
            "Fine balance should be reduced by 20");
    }

    @Test
    void testPayFineDoesNotGoNegative() {
        MemberService service = new MemberService();
        Member m = new Member("M2", "Bob", "bob@example.com");
        m.setfineBalance(10.0);
        service.addMember(m);

        service.payFine("M2", 50.0); // pay more than balance

        assertEquals(0.0, service.findById("M2").getFineBalance(),
            "Fine balance should not go below 0");
    }

    @Test
    void testPayFineNonexistentMemberDoesNothing() {
        MemberService service = new MemberService();
        service.payFine("M3", 10.0); // no member added

        assertNull(service.findById("M3"),
            "Nonexistent member should remain null");
    }
    
    @Test
    void testLoadMembersHandlesMissingFile() {
        MemberService service = new MemberService();

        service.loadMembers("nonexistent_file.csv");

        assertTrue(service.getAllMembers().isEmpty(),
            "Service should start fresh when file is missing");
    }
    
    @Test
    void testSaveMembersHandlesIOException() {
        MemberService service = new MemberService();
        Member m = new Member("M4", "Charlie", "charlie@example.com");
        service.addMember(m);

        // Try to save to a directory instead of a file
        service.saveMembers("/");  // On most systems this will fail

        // No exception should escape, members should still be in memory
        assertFalse(service.getAllMembers().isEmpty(),
            "Members should remain in memory even if saving fails");
    }
	
	
}
