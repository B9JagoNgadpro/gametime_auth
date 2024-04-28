package jagongadpro.autentikasi.repository;

import jagongadpro.autentikasi.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
}
