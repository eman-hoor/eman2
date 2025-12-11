import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailNotifierTest {

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
    void testEmailNotifierDelegatesToEmailService() {
        // Mock EmailService
        EmailService mockEmailService = mock(EmailService.class);

        // Create notifier with mock service
        EmailNotifier notifier = new EmailNotifier(mockEmailService);

        // Create a member
        Member m = new Member("M1", "Alice", "alice@example.com");

        // Call notify
        notifier.notify(m, "Test message");

        // Verify EmailService was called correctly
        verify(mockEmailService, times(1))
            .sendEmail(eq("alice@example.com"), eq("Library Reminder"), eq("Test message"));
    }
}
