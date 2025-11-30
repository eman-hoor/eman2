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
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

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
	
	
	
}
