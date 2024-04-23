package jagongadpro.autentikasi.dto;

import jagongadpro.autentikasi.service.ValidationService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoginUserRequestTest {
    @Autowired
    ValidationService validationService;
    @Test
    void LoginUserRequestSuccess(){
        LoginUserRequest user = new LoginUserRequest("abc@gmail.com", "password");
        validationService.validate(user);
        assertEquals(user.getEmail(),"abc@gmail.com" );
        assertEquals(user.getPassword(), "password");
    }

    @Test
    void LoginUserRequestEmailBlank(){
        LoginUserRequest user = new LoginUserRequest("", "password");
        assertThrows(ConstraintViolationException.class, ()->{
            validationService.validate(user);
        });
    }
    @Test
    void LoginUserRequestPasswordBlank(){
        LoginUserRequest user = new LoginUserRequest("abc@gmail.com", "");
        assertThrows(ConstraintViolationException.class, ()->{
            validationService.validate(user);
        });
    }
    @Test
    void LoginUserRequestPasswordNull(){
        LoginUserRequest user = new LoginUserRequest("abc@gmail.com", null);
        assertThrows(ConstraintViolationException.class, ()->{
            validationService.validate(user);
        });
    }
    @Test
    void LoginUserRequestEmailNull(){
        LoginUserRequest user = new LoginUserRequest(null, "password");
        assertThrows(ConstraintViolationException.class, ()->{
            validationService.validate(user);
        });
    }
}