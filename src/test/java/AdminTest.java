import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class AdminTest {
 
	
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
