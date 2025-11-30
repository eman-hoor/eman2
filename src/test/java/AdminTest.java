import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminTest {

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
    void testAdminConstructorAndGetters() {
        Admin admin = new Admin("adminUser", "securePass");

        assertEquals("adminUser", admin.getUsername());
        assertEquals("securePass", admin.getPassword());
    }
	
	@Test
    void testAdminWithEmptyValues() {
        Admin admin = new Admin("", "");
        assertEquals("", admin.getUsername());
        assertEquals("", admin.getPassword());
    }

    @Test
    void testAdminDifferentValues() {
        Admin admin = new Admin("john", "doe123");
        assertNotEquals("admin", admin.getUsername());
        assertNotEquals("password123", admin.getPassword());
    }
	
	

}
