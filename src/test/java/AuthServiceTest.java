import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthServiceTest {


	@Test
	void testValidLogin() {
		
		AuthService auth = new AuthService(); 
        assertTrue(auth.login("hoor", "hoor122"));
        assertTrue(auth.isLoggedIn());
	}
	
	@Test
    void testInvalidLogin() {
        AuthService auth = new AuthService();
        assertFalse(auth.login("ahmad", "wrong"));
        assertFalse(auth.isLoggedIn());
    }
	
	@Test
    void testLogout() {
        AuthService auth = new AuthService();
        auth.login("hoor", "hoor122");
        auth.logout();
        assertFalse(auth.isLoggedIn());
    }

	@Test
	void testEmptyCredentials() {
	    AuthService auth = new AuthService();
	    assertFalse(auth.login("", ""));
	}
	
	
	@Test
    void testInvalidLoginWrongUsername() {
        AuthService auth = new AuthService();
        assertFalse(auth.login("wrongUser", "hoor122"));
        assertFalse(auth.isLoggedIn());
    }

    @Test
    void testInvalidLoginWrongPassword() {
        AuthService auth = new AuthService();
        assertFalse(auth.login("hoor", "wrongPass"));
        assertFalse(auth.isLoggedIn());
    }
    
    @Test
    void testLogoutWithoutLogin() {
        AuthService auth = new AuthService();
        auth.logout(); // should not throw error
        assertFalse(auth.isLoggedIn());
    }
    
    @Test
    void testAddDuplicateAdminThrowsException() {
        AuthService authService;
        authService = new AuthService();
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> authService.addAdmin("hoor", "anotherPass"));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void testCaseInsensitiveLogin() {
    	AuthService authService;
        authService = new AuthService();
        // login should work regardless of case
        assertTrue(authService.login("HoOr", "hoor122"));
        assertEquals("hoor", authService.getCurrentAdmin().getUsername());
    }

    @Test
    void testAdminLoginSuccess() {
        AuthService authService = new AuthService();
        authService.addAdmin("admin", "pass");

        assertTrue(authService.login("admin", "pass"));
        assertEquals("admin", authService.getCurrentAdmin().getUsername());
    }

}
