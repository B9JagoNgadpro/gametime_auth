package jagongadpro.autentikasi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jagongadpro.autentikasi.dto.LoginResponse;
import jagongadpro.autentikasi.dto.LoginUserRequest;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.repository.UserRepository;
import jagongadpro.autentikasi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    ObjectMapper objectMapper;
    //test sukses
    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void LoginSuccess() throws  Exception {
        User user = new User.Builder().email("abc@gmail.com").password(passwordEncoder.encode("password")).saldo(90000).build();
        userRepository.save(user);
        LoginUserRequest request = new LoginUserRequest("abc@gmail.com", "password");
        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<LoginResponse>>() {
                    });
                    assertNotNull(response.getData().getToken());
                    String role = jwtService.extractRole(response.getData().getToken());
                    Integer saldo = jwtService.extractSaldo(response.getData().getToken());
                    assertEquals(saldo, 90000);
                    assertEquals(role, "PEMBELI");
                    assertNotNull(response.getData().getExpiredIn());
                    assertNull(response.getErrors());
                });
    }

    @Test
    void LoginFailedRequestNotValid() throws  Exception {
        User user = new User.Builder().email("abc@gmail.com").password(passwordEncoder.encode("password")).build();
        userRepository.save(user);
        LoginUserRequest request = new LoginUserRequest("abc@gmail.com", "");
        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                    assertNull(response.getData());
                });
    }

    @Test
    void LoginFailedNotBadCredentials() throws  Exception {
        User user = new User.Builder().email("abc@gmail.com").password(passwordEncoder.encode("password")).build();
        userRepository.save(user);
        LoginUserRequest request = new LoginUserRequest("abc@gmail.com", "salah");
        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                    assertNull(response.getData());
                    assertEquals(response.getErrors(), "Email atau password salah");
                });
    }
}