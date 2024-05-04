package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.repository.PasswordResetTokenRepository;
import jagongadpro.autentikasi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    @Transactional
    public void createPasswordResetTokenForUser(User user, String token) {
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
}
