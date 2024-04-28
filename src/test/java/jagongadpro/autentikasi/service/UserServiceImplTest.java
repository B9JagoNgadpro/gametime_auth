package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.repository.PasswordResetTokenRepository;
import jagongadpro.autentikasi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    void userFindByEmailFound(){
        String email = "abc@gmail.com";
        User user = new User.Builder().email(email).username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        User userGet = userService.findByEmail(email);
        assertNotNull(userGet);
        assertEquals(userGet.getEmail(), user.getEmail());


    }
    @Test
    void userFindByEmailNotFound(){
        String email = "abc@gmail.com";
        User userGet = userService.findByEmail(email);
        assertNull(userGet);
    }

    @Test
    void savePasswordResetTokenSuccees(){
        userService.createPasswordResetTokenForUser( new User(),"token");
        verify(passwordResetTokenRepository,times(1)).save(any(PasswordResetToken.class));

    }
}