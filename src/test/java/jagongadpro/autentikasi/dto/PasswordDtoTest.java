package jagongadpro.autentikasi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordDtoTest {
    PasswordDto passwordDto = new PasswordDto();

    @Test
    void testGetterSetterToken(){
        passwordDto.setToken("token");
        assertEquals(passwordDto.getToken(), "token");
    }
    @Test
    void testGetterSetterPassword(){
        passwordDto.setNewPassword("newPassword");
        assertEquals(passwordDto.getNewPassword(), "newPassword");
    }
}