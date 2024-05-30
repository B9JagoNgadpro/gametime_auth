package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceImplTest {
    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    PasswordResetTokenServiceImpl passwordResetTokenService;


    @Test
    void testPasswordResetTokenValid() {
        User user = new User();
        PasswordResetToken passwordResetToken = new PasswordResetToken("token", user);
        when(passwordResetTokenRepository.findByToken("token")).thenReturn(Optional.of(passwordResetToken));
        String result = passwordResetTokenService.validatePasswordResetToken("token");
        assertEquals("valid", result);

    }

    @Test
    void testPasswordResetTokenNotFound() {
        String token = "token";
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        assertEquals("invalid", result);

    }

    @Test
    void testPasswordResetTokenExpired() {
        User user = new User();
        String token = "token";

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -1);
        Date expiryDate = cal.getTime();
        passwordResetToken.setExpiryDate(expiryDate);
        when(passwordResetTokenRepository.findByToken("token")).thenReturn(Optional.of(passwordResetToken));
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        assertEquals("invalid" , result);

    }

    @Test
    void getUserByPasswordResetTokenTest() {
        User user = new User();
        user.setUsername("abc");
        PasswordResetToken passwordResetToken = new PasswordResetToken("token", user);
        when(passwordResetTokenRepository.findByToken("token")).thenReturn(Optional.of(passwordResetToken));
        User getUser = passwordResetTokenService.getUserByPasswordResetToken("token");
        assertEquals("abc", getUser.getUsernameReal());
    }
}