package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.repository.PasswordResetTokenRepository;
import jagongadpro.autentikasi.repository.UserRepository;
import jagongadpro.autentikasi.enums.Status;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    PasswordEncoder passwordEncoder;
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

    @Test
    void changeUserPasswordTest(){
        User user = new User();
        when(passwordEncoder.encode("newPassword")).thenReturn("encode");
        userService.changeUserPassword(user, "newPassword");
        verify(userRepository,times(1)).save(any(User.class));
        assertNotNull(user.getPassword());
        assertEquals(user.getPassword(), "encode");
    }

    @Test
    void reduceBalanceSuccess(){
        String email = "abc@gmail.com";
        User user = new User.Builder().email(email).saldo(10000).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        userService.reduceBalance(email, 20000);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(user);
        assertEquals(user.getSaldo(),20000);
    }

    @Test
    void reduceBalanceFailed(){
        assertThrows(UserNotFoundException.class,()-> userService.reduceBalance("email", 2000) );
    }

    @Test
    public void testChangeUserRole() {
        // Arrange
        User existingUser = new User.Builder().email("test@example.com").status(Status.ROLE_PEMBELI).build();;
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("test@example.com");
        when(auth.getCredentials()).thenReturn("password");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Act
        userService.changeUserRole("test@example.com", Status.ROLE_PENJUAL);

        // Assert
        assertEquals(Status.ROLE_PENJUAL, existingUser.getStatus());


    }

    @Test
    void changeUserRoleToSameRoleThrowsException() {
        String email = "user@example.com";
        User user = new User.Builder().email(email).username("username").bio("bio").password("password").profileUrl("url").saldo(500000).status(Status.ROLE_PENJUAL).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> userService.changeUserRole(email, Status.ROLE_PENJUAL));
    }

    @Test
    void changeUserRoleUserNotFoundThrowsException() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changeUserRole(email, Status.ROLE_PENJUAL));
    }
}