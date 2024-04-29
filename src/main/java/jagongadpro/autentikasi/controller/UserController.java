package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.service.*;
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
    UserFacade userFacade;

    @PostMapping(value = "/user/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> resetPassword(HttpServletRequest request,@RequestParam("email") String userEmail) {
        return userFacade.resetPassword(request, userEmail);
    }


    @GetMapping("/user/changePassword")
    public ResponseEntity<String> showChangePasswordPage(@RequestParam("token") String token) {
        return userFacade.showChangePasswordPage(token);
    }
    @PostMapping(value = "/user/savePassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> savePassword(@RequestBody PasswordDto passwordDto) {
        return userFacade.savePassword(passwordDto);
    }

}
