import static org.junit.jupiter.api.Assertions.*;


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
