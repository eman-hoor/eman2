import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testSendEmailFailsWithInvalidCredentials {

	

	@Test
	void testSendEmailFailsWithInvalidCredentials() {
	    EmailService emailService = new EmailService("fakeUser", "fakePass");
	    assertThrows(RuntimeException.class, () -> {
	        emailService.sendEmail("test@example.com", "Subject", "Body");
	    });
	}

}
