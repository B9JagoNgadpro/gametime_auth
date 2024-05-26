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
    LoginUserRequest loginUserRequest = new LoginUserRequest("abc@gmail.com", "password");

    @Test
    void getterEmail(){
        assertEquals("abc@gmail.com", loginUserRequest.getEmail());

    }
    @Test
    void getterPassword(){
        assertEquals("password", loginUserRequest.getPassword());

    }

    @Test
    void setterEmail(){
        loginUserRequest.setEmail("new@gmail.com");
        assertEquals("new@gmail.com", loginUserRequest.getEmail());

    }
    @Test
    void setterPassword(){
        loginUserRequest.setPassword("passwordnew");
        assertEquals("passwordnew", loginUserRequest.getPassword());

    }
    @Test
    void LoginUserRequestSuccess(){
        LoginUserRequest user = new LoginUserRequest("abc@gmail.com", "password");
        validationService.validate(user);
        assertEquals("abc@gmail.com", user.getEmail() );
        assertEquals("password" , user.getPassword());
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