package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.service.EmailServiceImpl;
import jagongadpro.autentikasi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.codec.ServerSentEvent.builder;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailServiceImpl emailService;

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

}
