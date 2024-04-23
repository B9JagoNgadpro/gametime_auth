package jagongadpro.autentikasi.service;


import jagongadpro.autentikasi.dto.LoginUserRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ValidationServiceTest {
    @Autowired
    ValidationService validationService;
    LoginUserRequest request;
    @Test
    void testValidationWorks(){
        request = new LoginUserRequest(null, null);

        Throwable exception = assertThrows(ConstraintViolationException.class, () -> {
            validationService.validate(request);
        });

    }

}