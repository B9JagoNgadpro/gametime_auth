package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.service.EmailServiceImpl;
import jagongadpro.autentikasi.service.PasswordResetTokenServiceImpl;
import jagongadpro.autentikasi.service.UserService;
import jagongadpro.autentikasi.service.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ValidationService validationService;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    PasswordResetTokenServiceImpl passwordResetTokenService;

    @PostMapping(value = "/user/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> resetPassword(HttpServletRequest request,
                                             @RequestParam("email") String userEmail) {
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
    @GetMapping("/user/changePassword")
    public ResponseEntity<String> showChangePasswordPage(@RequestParam("token") String token) {
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        if(result.equals("invalid")) {
            //nanti return redirect ke login page karna ga valid
            return new ResponseEntity<>("invalid", HttpStatus.NOT_FOUND);
            //valid nanti return ke halaman forget pw
        } else {
            return ResponseEntity.ok().body("valid");
        }
    }
    @PostMapping(value = "/user/savePassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> savePassword(@RequestBody PasswordDto passwordDto) {
        validationService.validate(passwordDto);
        String result = passwordResetTokenService.validatePasswordResetToken(passwordDto.getToken());

        if(result != null) {
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
