package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    public void testConstructResetTokenEmail() {
        String contextPath = "http://example.com";
        String token = "myToken";
        User user = new User.Builder().email("test@example.com").build();
        emailService.constructResetTokenEmail(contextPath, token, user);
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));

    }

    @Test
    public void testConstructEmail() {
        String subject = "Test Subject";
        String body = "Test Body";
        User user = new User.Builder().email("abc@gmail.com").build();

        SimpleMailMessage email = emailService.constructEmail(subject, body, user);
        assertEquals(subject, email.getSubject());
        assertEquals(body, email.getText());
        assertNotNull( email.getTo()[0]);
        assertEquals(user.getEmail(), email.getTo()[0]);
        assertEquals("gametimejagongadpro@gmail.com", email.getFrom());
        assertNotNull(email);
    }
}
