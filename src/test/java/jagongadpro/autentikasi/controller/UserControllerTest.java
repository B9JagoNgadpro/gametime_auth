package jagongadpro.autentikasi.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Locale;
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

    @InjectMocks
    private UserController userController;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testResetPassword() throws Exception {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        User user = new User.Builder().email(email).username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();

        when(userService.findByEmail(email)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/resetPassword")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/user/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    assertEquals(response.getErrors(), "Email tidak ditemukan" );
                });

    }
}