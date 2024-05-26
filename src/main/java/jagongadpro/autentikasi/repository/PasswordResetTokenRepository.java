package jagongadpro.autentikasi.repository;

import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
    void deleteByToken(String token);

}
