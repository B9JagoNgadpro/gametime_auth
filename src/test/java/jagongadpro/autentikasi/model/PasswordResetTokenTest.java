package jagongadpro.autentikasi.model;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {
    PasswordResetToken passwordResetToken = new PasswordResetToken();
    User user = new User();
    @Test
    void getterSetterToken(){
        passwordResetToken.setToken("token");
        assertEquals("token", passwordResetToken.getToken());

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