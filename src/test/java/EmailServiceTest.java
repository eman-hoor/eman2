import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.mail.Message;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class EmailServiceTest {

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
    void testSendEmail() throws Exception {
        EmailService emailService = new EmailService("test@gmail.com", "password");

        // Mock Transport.send (static method)
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                         .thenAnswer(invocation -> {
                             Message msg = invocation.getArgument(0);
                             System.out.println("Mock send: " + msg.getSubject());
                             return null;
                         });

            // Call your method
            emailService.sendEmail("user@example.com", "Test Subject", "Hello World");

            // Verify Transport.send was called once
            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

}
