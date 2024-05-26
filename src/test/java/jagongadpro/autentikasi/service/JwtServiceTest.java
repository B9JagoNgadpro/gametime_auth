package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {
    @Autowired
    JwtService jwtService;
    User userLogin = new User.Builder().email("abc@gmail.com").username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();
    @Test
    void extractUsernameTest() {
        String token = jwtService.generateToken(userLogin);
        assertEquals("abc@gmail.com", jwtService.extractUsername(token) );
    }

    @Test
    void testGenerateTokenTest() {
        String token = jwtService.generateToken(userLogin);
        assertNotNull(token);
    }

    @Test
    void getExpirationTimeTest() {
        assertEquals(3000000L, jwtService.getExpirationTime());
    }

    @Test
    void isTokenValidTestSuccess() {
        String token = jwtService.generateToken(userLogin);
        assertTrue(jwtService.isTokenValid(token,userLogin));
    }

    @Test
    void isTokenValidTestNotSameUser() {
        User other = new User.Builder().email("other@gmail.com").build();
        String token = jwtService.generateToken(userLogin);
        assertFalse(jwtService.isTokenValid(token,other));
    }
}