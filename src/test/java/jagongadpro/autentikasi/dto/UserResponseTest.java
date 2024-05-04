package jagongadpro.autentikasi.dto;

import jagongadpro.autentikasi.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {
    UserResponse userResponse = UserResponse.builder().email("abc@gmail.com").username("username").saldo(10000).status(Status.ROLE_PEMBELI).build();

    @Test
    void getterEmail(){
        assertEquals(userResponse.getEmail(), "abc@gmail.com");
    }
    @Test
    void getterUserName(){
        assertEquals(userResponse.getUsername(), "username");
    }
    @Test
    void getterSaldo(){
        assertEquals(userResponse.getSaldo(), 10000);
    }
    @Test
    void getterStatus(){
        assertEquals(userResponse.getStatus(), Status.ROLE_PEMBELI);
    }
    @Test
    void toStringTest(){
    assertNotNull(UserResponse.builder().toString());
    }

}