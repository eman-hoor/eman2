import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testSendEmailFailsWithInvalidCredentials {

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
	void testSendEmailFailsWithInvalidCredentials() {
	    EmailService emailService = new EmailService("fakeUser", "fakePass");
	    assertThrows(RuntimeException.class, () -> {
	        emailService.sendEmail("test@example.com", "Subject", "Body");
	    });
	}

}
