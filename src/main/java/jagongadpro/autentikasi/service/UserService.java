package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.enums.Status;

public interface UserService {
    User findByEmail(String email);
    void createPasswordResetTokenForUser(User user, String token);
    void changeUserPassword(User user, String newPassword);
    void reduceBalance(String email, Integer newBalance);
    void addBalance(String email, Integer newBalance);
    void changeUserRole(String email, Status newRole);
    void updateProfileUrl(String email, String profileUrl);
    void updateBio(String email, String bio);    
}