package jagongadpro.autentikasi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {
    LoginResponse loginResponse = new LoginResponse();
    @Test
    void SetterGetterTokenTest(){
        loginResponse.setToken("token");
        assertEquals("token", loginResponse.getToken());
    }
    @Test
    void SetterGetterExpiredTest(){
        loginResponse.setExpiredIn(3000l);
        assertEquals(3000l, loginResponse.getExpiredIn());
    }


}