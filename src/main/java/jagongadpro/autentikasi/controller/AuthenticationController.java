package jagongadpro.autentikasi.controller;

import jagongadpro.autentikasi.dto.LoginResponse;
import jagongadpro.autentikasi.dto.LoginUserRequest;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.service.AuthenticationService;
import jagongadpro.autentikasi.service.JwtService;
import jagongadpro.autentikasi.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("api/auth/")
public class AuthenticationController {
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ValidationService validationService;

    @PostMapping(value = "/api/auth/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<LoginResponse> login(@RequestBody LoginUserRequest request){
        //contraint violation exception
        validationService.validate(request);
        //bisa ada eror bad doncern bla"gitu dah
        User authenticatedUser = authenticationService.authenticate(request);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiredIn(jwtService.getExpirationTime());

        return WebResponse.<LoginResponse>builder().data(loginResponse).build();

    }
}