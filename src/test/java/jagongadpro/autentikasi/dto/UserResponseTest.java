package jagongadpro.autentikasi.dto;

import jagongadpro.autentikasi.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {
    UserResponse userResponse = UserResponse.builder().email("abc@gmail.com").username("username").saldo(10000).status(Status.ROLE_PEMBELI).build();

    @Test
    void getterEmail(){
        assertEquals("abc@gmail.com", userResponse.getEmail());
    }
    @Test
    void getterUserName(){
        assertEquals("username", userResponse.getUsername());
    }
    @Test
    void getterSaldo(){
        assertEquals(10000, userResponse.getSaldo());
    }
    @Test
    void getterStatus(){
        assertEquals(Status.ROLE_PEMBELI, userResponse.getStatus());
    }
    @Test
    void toStringTest(){
    assertNotNull(UserResponse.builder().toString());
    }

}