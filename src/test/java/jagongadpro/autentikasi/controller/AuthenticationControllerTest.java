package jagongadpro.autentikasi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jagongadpro.autentikasi.dto.LoginResponse;
import jagongadpro.autentikasi.dto.LoginUserRequest;
import jagongadpro.autentikasi.dto.WebResponse;

import jagongadpro.autentikasi.model.User;

import jagongadpro.autentikasi.service.AuthenticationService;
import jagongadpro.autentikasi.service.EmailServiceImpl;
import jagongadpro.autentikasi.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    AuthenticationController authenticationController;
    @MockBean
    JwtService jwtService;
    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    EmailServiceImpl emailService;

    @Autowired
    ObjectMapper objectMapper;
    //test sukses

    @Test
    void LoginSuccess() throws  Exception {
        User user = new User.Builder().email("abc@gmail.com").password("password").saldo(90000).build();
        LoginUserRequest request = new LoginUserRequest("abc@gmail.com", "password");
        when(authenticationService.authenticate(request)).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<LoginResponse>>() {
                    });
                    assertNotNull(response.getData());
                    assertNull(response.getErrors());
                });
    }

    @Test
    void LoginFailedBadCredentials() throws  Exception {
        User user = new User.Builder().email("abc@gmail.com").password("password").saldo(90000).build();
        LoginUserRequest request = new LoginUserRequest("abc@gmail.com", "password");
        when(authenticationService.authenticate(any(LoginUserRequest.class))).thenThrow(new BadCredentialsException("Email atau password salah"));
        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                    assertEquals(response.getErrors(),"Email atau password salah");
                });
    }

    @Test
    void LoginFailedInputNotValid() throws  Exception {
        User user = new User.Builder().email("abc@gmail.com").password("password").saldo(90000).build();
        LoginUserRequest request = new LoginUserRequest("abc@gmail.com","");
        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());

                });
    }
}