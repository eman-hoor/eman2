import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    void testMemberConstructorAndGetters() {
        Member member = new Member("M1", "Alice", "alice@mail.com");

        assertEquals("M1", member.getMemberId());
        assertEquals("Alice", member.getName());
        assertEquals("alice@mail.com", member.getContact());
        assertEquals(0.0, member.getFineBalance());
    }
	
	@Test
    void testAddFineIncreasesBalance() {
        Member member = new Member("M1", "Alice", "alice@mail.com");
        member.addFine(5.0);

        assertEquals(5.0, member.getFineBalance());
    }

    @Test
    void testPayFineReducesBalance() {
        Member member = new Member("M1", "Alice", "alice@mail.com");
        member.addFine(10.0);
        member.payFine(4.0);

        assertEquals(6.0, member.getFineBalance());
    }
    
    @Test
    void testPayFineMoreThanBalanceSetsZero() {
        Member member = new Member("M1", "Alice", "alice@mail.com");
        member.addFine(5.0);
        member.payFine(10.0);

        assertEquals(0.0, member.getFineBalance());
    }

    @Test
    void testToString() {
        Member m = new Member("M1", "Alice", "alice@example.com");
        m.setfineBalance(25.0);

        String expected = "M1 - Alice (alice@example.com), Fine: 25.0";
        assertEquals(expected, m.toString(),
            "toString should return member details in the correct format");
    }
}
