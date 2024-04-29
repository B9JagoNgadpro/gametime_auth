package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.User;

public interface UserService {
   User findByEmail(String email);
    void createPasswordResetTokenForUser(User user, String token);
    void changeUserPassword(User user, String newPassword);
}
