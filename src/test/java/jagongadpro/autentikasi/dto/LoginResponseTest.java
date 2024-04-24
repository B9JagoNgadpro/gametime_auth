package jagongadpro.autentikasi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {
    LoginResponse loginResponse = new LoginResponse();
    @Test
    void SetterGetterTokenTest(){
        loginResponse.setToken("token");
        assertEquals(loginResponse.getToken(), "token");
    }
    @Test
    void SetterGetterExpiredTest(){
        loginResponse.setExpiredIn(3000l);
        assertEquals(loginResponse.getExpiredIn(), 3000l);
    }


}