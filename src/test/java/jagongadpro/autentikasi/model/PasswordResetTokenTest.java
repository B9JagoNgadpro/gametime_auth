package jagongadpro.autentikasi.model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {
    PasswordResetToken passwordResetToken = new PasswordResetToken();
    User user = new User();
    @Test
    void getterSetterToken(){
        passwordResetToken.setToken("token");
        assertEquals(passwordResetToken.getToken(), "token");

    }

    @Test
    void getterSetterUser(){
        passwordResetToken.setUser(user);
        assertEquals(passwordResetToken.getUser(), user);

    }
    @Test
    void getterExpiryDate(){
       assertNotNull( passwordResetToken.getExpiryDate());
    }
}