package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.dto.LoginUserRequest;
import jagongadpro.autentikasi.dto.RegisterUserRequest;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    public User signup(RegisterUserRequest input) {
        User user = new User.Builder()
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .build();

        return userRepository.save(user);
    }

    //kalau salah jadi Bad Credemtials
    public User authenticate(LoginUserRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}