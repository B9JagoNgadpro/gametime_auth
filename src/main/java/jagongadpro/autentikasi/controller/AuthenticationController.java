package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.LoginResponse;
import jagongadpro.autentikasi.dto.LoginUserRequest;
import jagongadpro.autentikasi.dto.RegisterUserRequest;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.service.AuthenticationService;
import jagongadpro.autentikasi.service.EmailServiceImpl;
import jagongadpro.autentikasi.service.JwtService;
import jagongadpro.autentikasi.service.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;

@Configuration
class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ValidationService validationService;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<LoginResponse> login(@RequestBody LoginUserRequest request){
        // Contraint violation exception
        validationService.validate(request);
        // Bisa ada error bad doncern bla"gitu dah
        User authenticatedUser = authenticationService.authenticate(request);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiredIn(jwtService.getExpirationTime());

        return WebResponse.<LoginResponse>builder().data(loginResponse).build();
    }

    @PostMapping(value = "signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        // Untuk membuat keranjang secara otomatis
        String email = registeredUser.getEmail();
        String apiUrl = "http://35.213.132.17/api/cart/create/" + email;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return ResponseEntity.ok(registeredUser);
    }
}
