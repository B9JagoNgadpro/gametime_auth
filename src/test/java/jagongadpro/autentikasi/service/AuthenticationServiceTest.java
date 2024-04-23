package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.dto.LoginUserRequest;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationServiceTest {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void AuthenticateSuccess(){
        User user = new User.Builder().email("hai@gmail.com").password(passwordEncoder.encode("password") ).build();
        userRepository.save(user);
        LoginUserRequest request = new LoginUserRequest("hai@gmail.com", "password");
        User login = authenticationService.authenticate(request);
        assertEquals(login.getEmail(), user.getEmail() );
    }

    @Test
    void WrongPassword(){
        User user = new User.Builder().email("hai@gmail.com").password(passwordEncoder.encode("password") ).build();
        userRepository.save(user);
        LoginUserRequest request = new LoginUserRequest("hai@gmail.com", "wrong");
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(request);
        });
    }

    @Test
    void WrongEmail(){
        User user = new User.Builder().email("hai@gmail.com").password(passwordEncoder.encode("password") ).build();
        userRepository.save(user);
        LoginUserRequest request = new LoginUserRequest("halo@gmail.com", "wrong");
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(request);
        });
    }
}