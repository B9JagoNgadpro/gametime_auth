package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class UserFacade {
    @Autowired
    UserService userService;

    @Autowired
    ValidationService validationService;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    PasswordResetTokenServiceImpl passwordResetTokenService;

    public WebResponse<String> resetPassword(HttpServletRequest request, String userEmail){
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException("Email tidak ditemukan");
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        emailService.constructResetTokenEmail(getAppUrl(request), token, user);
        return WebResponse.<String>builder().data("url sent").build();

    }
    private String getAppUrl(HttpServletRequest request) {
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }

    public ResponseEntity<String> showChangePasswordPage(String token){
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        if(result.equals("invalid")) {
            //nanti return redirect ke login page karna ga valid
            return new ResponseEntity<>("invalid", HttpStatus.NOT_FOUND);
            //valid nanti return ke halaman forget pw
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "http://gametime-frontend.vercel.app/changePassword?token="+token);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }

    public  WebResponse<String> savePassword(PasswordDto passwordDto){
        validationService.validate(passwordDto);
        String result = passwordResetTokenService.validatePasswordResetToken(passwordDto.getToken());

        if(result.equals("invalid")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "token tidak valid");
        }
        User user = passwordResetTokenService.getUserByPasswordResetToken(passwordDto.getToken());
        if(user!=null) {
            userService.changeUserPassword(user, passwordDto.getNewPassword());
            return WebResponse.<String>builder().data("password berhasil diganti").build();
        } else {
            throw new UserNotFoundException("user tidak ditemukan");
        }
    }
}
