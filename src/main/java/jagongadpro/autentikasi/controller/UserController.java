package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.UserResponse;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.PasswordResetToken;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.service.*;
import jagongadpro.autentikasi.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserFacade userFacade;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    UserService userService;

    @PostMapping(value = "/user/password/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> resetPassword(HttpServletRequest request,@RequestParam("email") String userEmail) {
        return userFacade.resetPassword(request, userEmail);
    }

    @GetMapping("/user/password/changePassword")
    public ResponseEntity<String> showChangePasswordPage(@RequestParam("token") String token) {
        return userFacade.showChangePasswordPage(token);
    }

    @PostMapping(value = "/user/password/savePassword", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> savePassword(@RequestBody PasswordDto passwordDto) {
        return userFacade.savePassword(passwordDto);
    }

    @GetMapping("/user/me")
    public WebResponse<UserResponse> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByEmail(user.getUsername());
        UserResponse userResponse = UserResponse.builder().username(currentUser.getUsernameReal()).email(currentUser.getEmail()).saldo(currentUser.getSaldo()).status(currentUser.getStatus()).build();
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PatchMapping(value = "/user/reduceBalance"  )
    public WebResponse<String> setUserBalance(@RequestBody Map<String, Integer> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        if (request.get("saldo")<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo tidak valid");
        }
        userService.reduceBalance(user.getUsername(), request.get("saldo"));
        return WebResponse.<String>builder().data("Ok").build();
    }

    @PostMapping("/api/user/changeRole")
    public ResponseEntity<WebResponse<String>> changeUserRole(@AuthenticationPrincipal UserDetails currentUser,
                                                              @RequestParam("newRole") String newRole) {
        String email = currentUser.getUsername();
        userService.changeUserRole(email, Status.valueOf(newRole));
        return ResponseEntity.ok(WebResponse.<String>builder().data("Role updated successfully").build());
    }

    @PatchMapping(value = "/user/updateProfileUrl", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> updateProfileUrl(@RequestBody Map<String, String> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String profileUrl = request.get("profileUrl");

        if (profileUrl == null || profileUrl.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile URL tidak boleh kosong");
        }

        userService.updateProfileUrl(user.getUsername(), profileUrl);
        return WebResponse.<String>builder().data("Profile URL updated successfully").build();
    }

    @PatchMapping(value = "/user/updateBio", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> updateBio(@RequestBody Map<String, String> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String bio = request.get("bio");

        if (bio == null || bio.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bio tidak boleh kosong");
        }

        userService.updateBio(user.getUsername(), bio);
        return WebResponse.<String>builder().data("Bio updated successfully").build();
    }

}