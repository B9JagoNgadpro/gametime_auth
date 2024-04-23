package jagongadpro.autentikasi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginUserRequestTest {
    @Test
    void LoginUserRequestSuccess(){
        LoginUserRequest user = new LoginUserRequest("abc@gmail.com", "password");
        assertEquals(user.getEmail(),"abc@gmail.com" );
        assertEquals(user.getPassword(), "password");
    }

    @Test
    void LoginUserRequestEmailNull(){
        LoginUserRequest user = new LoginUserRequest(null, "password");

    }
}