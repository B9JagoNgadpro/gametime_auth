package jagongadpro.autentikasi.config;

import jagongadpro.autentikasi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ApplicationConfigurationTest {

    @Mock
    private UserRepository userRepository;



    @Test
    void userDetailsServiceBean() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.empty());
        ApplicationConfiguration config = new ApplicationConfiguration(userRepository);
        UserDetailsService userDetailsService = config.userDetailsService();

        assertNotNull(userDetailsService);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@example.com"));
    }

    @Test
    void passwordEncoderBean() {
        ApplicationConfiguration config = new ApplicationConfiguration(userRepository);
        BCryptPasswordEncoder passwordEncoder = config.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    void authenticationManagerBean(@Autowired AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ApplicationConfiguration config = new ApplicationConfiguration(userRepository);
        AuthenticationManager authenticationManager = config.authenticationManager(authenticationConfiguration);
        assertNotNull(authenticationManager);
    }

    @Test
    void authenticationProviderBean() {
        ApplicationConfiguration config = new ApplicationConfiguration(userRepository);
        AuthenticationProvider authenticationProvider = config.authenticationProvider();
        assertNotNull(authenticationProvider);
    }
}