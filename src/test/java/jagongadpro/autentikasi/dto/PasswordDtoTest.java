package jagongadpro.autentikasi.dto;

import static org.junit.jupiter.api.Assertions.*;

class PasswordDtoTest {
    PasswordDto passwordDto = new PasswordDto();

    void testGetterSetterToken(){
        passwordDto.setToken("token");
        assertEquals(passwordDto.getToken(), "token");
    }
    void testGetterSetterPassword(){
        passwordDto.setNewPassword("newPassword");
        assertEquals(passwordDto.getNewPassword(), "newPassword");
    }
}