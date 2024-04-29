package jagongadpro.autentikasi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.service.PasswordResetTokenServiceImpl;
import jagongadpro.autentikasi.service.UserService;
import jagongadpro.autentikasi.service.ValidationService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @MockBean
    private PasswordResetTokenServiceImpl passwordResetTokenService;


    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testResetPassword() throws Exception {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        User user = new User.Builder().email(email).username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();

        when(userService.findByEmail(email)).thenReturn(user);

        mockMvc.perform(post("/user/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertEquals(response.getData(), "url sent" );
                });

    }

    @Test
    public void testResetPasswordEmailNotFound() throws Exception {
        String email = "test@example.com";
        mockMvc.perform(post("/user/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    assertEquals(response.getErrors(), "Email tidak ditemukan" );
                });

    }

    @Test
    public void testTokenValid() throws  Exception{
        when(passwordResetTokenService.validatePasswordResetToken("token")).thenReturn("valid") ;
        mockMvc.perform(get("/user/changePassword").param("token", "token"))
                .andExpect(status().isOk())
                .andDo(result ->{
                    String response = result.getResponse().getContentAsString();
                    assertEquals(response, "valid");
                } );

    }

    @Test
    public void testTokenInValid() throws  Exception{
        when(passwordResetTokenService.validatePasswordResetToken("token")).thenReturn("invalid") ;
        mockMvc.perform(get("/user/changePassword").param("token", "token"))
                .andExpect(status().isNotFound())
                .andDo(result ->{
                    String response = result.getResponse().getContentAsString();
                    assertEquals(response, "invalid");
                } );

    }

    @Test
    public void savePasswordSuccess() throws Exception{
        User user = new User();
        PasswordDto passwordDto = new PasswordDto("token", "newPassword");
        when(passwordResetTokenService.validatePasswordResetToken("token")).thenReturn(null) ;
        when(passwordResetTokenService.getUserByPasswordResetToken("token")).thenReturn(user) ;
        mockMvc.perform(post("/user/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isOk())
                .andDo(result -> {
                   WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                   });
                   assertEquals(response.getData(), "password berhasil diganti");
                });
    }

    @Test
    public void savePasswordArgumentNotvalid() throws Exception{
        User user = new User();
        PasswordDto passwordDto = new PasswordDto("token","");
        mockMvc.perform(post("/user/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNull(response.getData());
                    assertEquals(response.getErrors(), "newPassword: password tidak boleh kosong");
                });
    }

    @Test
    public void savePasswordTokenNotvalid() throws Exception{
        User user = new User();
        PasswordDto passwordDto = new PasswordDto("token","newPassword");
        when(passwordResetTokenService.validatePasswordResetToken("token")).thenReturn("invalid");
        mockMvc.perform(post("/user/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNull(response.getData());
                    assertEquals(response.getErrors(), "token tidak valid");
                });
    }
    @Test
    public void savePasswordUserNotFound() throws Exception{
        User user = new User();
        PasswordDto passwordDto = new PasswordDto("token","newPassword");
        when(passwordResetTokenService.validatePasswordResetToken("token")).thenReturn(null);
        when(passwordResetTokenService.getUserByPasswordResetToken("token")).thenReturn(null) ;
        mockMvc.perform(post("/user/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNull(response.getData());
                    assertEquals(response.getErrors(), "user tidak ditemukan");
                });
    }

}