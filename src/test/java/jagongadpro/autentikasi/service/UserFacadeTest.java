package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
    @InjectMocks
    UserFacade userFacade;
    @Mock
    ValidationService validationService;
    @Mock
    EmailServiceImpl emailServiceImpl;
    @Mock
    UserService userService;
    @Mock
    PasswordResetTokenServiceImpl passwordResetTokenService;

    @Test
    void resetPasswordTestSuccess(){
        String email = "email";
        User user = new User();
        when(userService.findByEmail(email)).thenReturn(user);
        HttpServletRequest request = mock(HttpServletRequest.class);
        WebResponse<String> response = userFacade.resetPassword(request, email);
        assertEquals("url sent", response.getData());
    }

    @Test
    void resetPasswordTestUserNotFound(){
        String email = "email";
        when(userService.findByEmail(email)).thenReturn(null);
        HttpServletRequest request = mock(HttpServletRequest.class);
        assertThrows(UserNotFoundException.class, ()-> userFacade.resetPassword(request, email));

    }

    @Test
    void showChangePasswordPageSuccess(){
        String token = "token";
        when(passwordResetTokenService.validatePasswordResetToken(token)).thenReturn("valid");
        ResponseEntity<String> response = userFacade.showChangePasswordPage(token);
        assertNotNull(response);
    }

    @Test
    void showChangePasswordPageFailed(){
        String token = "token";
        when(passwordResetTokenService.validatePasswordResetToken(token)).thenReturn("invalid");
        ResponseEntity<String> response = userFacade.showChangePasswordPage(token);
        assertEquals("invalid", response.getBody());
    }

    @Test
    void savePasswordSuccessTest(){
        String token = "token";
        User user = new User();
        PasswordDto passwordDto = new PasswordDto(token, "new-Password");
        when(passwordResetTokenService.validatePasswordResetToken(token)).thenReturn("valid");
        when(passwordResetTokenService.getUserByPasswordResetToken(token)).thenReturn(user);
        WebResponse<String> response = userFacade.savePassword(passwordDto);
        assertEquals("password berhasil diganti" , response.getData());

    }

    @Test
    void savePasswordValidatePasswordFailed(){
        String token = "token";
        PasswordDto passwordDto = new PasswordDto(token, "new-Password");
        when(passwordResetTokenService.validatePasswordResetToken(token)).thenReturn("invalid");
        assertThrows(ResponseStatusException.class, ()-> userFacade.savePassword(passwordDto));
    }
    @Test
    void savePasswordFailedUserNotFound(){
        String token = "token";
        PasswordDto passwordDto = new PasswordDto(token, "new-Password");
        when(passwordResetTokenService.validatePasswordResetToken(token)).thenReturn("valid");
        when(passwordResetTokenService.getUserByPasswordResetToken(token)).thenReturn(null);
        assertThrows(UserNotFoundException.class, ()-> userFacade.savePassword(passwordDto));
    }


}