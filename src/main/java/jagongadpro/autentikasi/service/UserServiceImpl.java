package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.enums.Status;
import jagongadpro.autentikasi.repository.PasswordResetTokenRepository;
import jagongadpro.autentikasi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordResetTokenServiceImpl passwordResetTokenService;

    @Override
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user).orElse(null);
        if(passwordResetToken!=null ){
            if (passwordResetTokenService.validatePasswordResetToken(token)=="valid"){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url already sent, please check your email");
            }
            else{
                passwordResetTokenRepository.deleteByToken(token);
            }

        }
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void reduceBalance(String email,  Integer newBalance) {
        User user =userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User tidak ditemukan"));
        user.setSaldo(newBalance);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeUserRole(String email, Status newRole) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.getStatus().equals(newRole)) {
            throw new IllegalStateException("Tidak dapat mengganti ke role yang sama.");
        }
        user.setStatus(newRole);
        userRepository.save(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
        updatedAuthorities.add(new SimpleGrantedAuthority(user.getStatus().name())); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Override
    @Transactional
    public void updateProfileUrl(String email, String profileUrl) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User tidak ditemukan"));
        user.setProfileUrl(profileUrl);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateBio(String email, String bio) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User tidak ditemukan"));
        user.setBio(bio);
        userRepository.save(user);
    }

}