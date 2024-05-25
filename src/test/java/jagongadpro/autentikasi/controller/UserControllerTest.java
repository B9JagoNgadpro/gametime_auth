package jagongadpro.autentikasi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.UserResponse;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.enums.Status;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;

import jagongadpro.autentikasi.service.PasswordResetTokenServiceImpl;
import jagongadpro.autentikasi.service.UserFacade;
import jagongadpro.autentikasi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.security.test.context.support.WithMockUser;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;
    @MockBean
    UserFacade userFacade;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    UserDetailsService userDetailsService;
    @MockBean
    PasswordResetTokenServiceImpl passwordResetTokenService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testResetPassword() throws Exception {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        User user = new User.Builder().email(email).username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();
        WebResponse<String> responseMock = WebResponse.<String>builder().data("url sent").build();
        when(userFacade.resetPassword(any(HttpServletRequest.class), eq(email))).thenReturn(responseMock);
        mockMvc.perform(post("/user/password/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertEquals(response.getData(), "url sent" );
                });
    }
    @Test
    public void testResetPasswordUserNotFound() throws Exception {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        User user = new User.Builder().email(email).username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();
        WebResponse<String> responseMock = WebResponse.<String>builder().data("url sent").build();
        when(userFacade.resetPassword(any(HttpServletRequest.class), eq(email))).thenThrow(new UserNotFoundException("Email tidak ditemukan"));
        mockMvc.perform(post("/user/password/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                    assertEquals(response.getErrors(),"Email tidak ditemukan");
                });
    }

    @Test
    public void showChangePasswordPageTestSuccess() throws  Exception{
        String token="token";
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("valid");
        when(userFacade.showChangePasswordPage(token)).thenReturn(responseEntity) ;
        mockMvc.perform(get("/user/password/changePassword").param("token", token))
                .andExpect(status().isOk())
                .andDo(result ->{
                    String response = result.getResponse().getContentAsString();
                    assertEquals(response, "valid");
                } );
    }

    @Test
    public void testTokenInValid() throws  Exception{
        when(passwordResetTokenService.validatePasswordResetToken("token")).thenReturn("invalid") ;
        mockMvc.perform(get("/user/password/changePassword").param("token", "token"))
                .andExpect(status().isNotFound())
                .andDo(result ->{
                    String response = result.getResponse().getContentAsString();
                    assertEquals(response, "invalid");
                } );
    }

    @Test
    public void savePasswordSuccess() throws Exception{
        User user = new User();
        WebResponse<String> responseMock = WebResponse.<String>builder().data("password berhasil diganti").build();
        PasswordDto passwordDto = new PasswordDto("token", "newPassword");
        when(userFacade.savePassword(any(PasswordDto.class))).thenReturn(responseMock) ;
        mockMvc.perform(post("/user/password/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isOk())
                .andDo(result -> {
                   WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                   });
                   assertEquals(response.getData(), "password berhasil diganti");
                });
    }

    @Test
    public void savePasswordArgumentNotvalid() throws Exception{
        User user = new User();
        PasswordDto passwordDto = new PasswordDto("token","");
        mockMvc.perform(post("/user/password/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isBadRequest())
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
        mockMvc.perform(post("/user/password/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isNotFound())
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
        mockMvc.perform(post("/user/password/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNull(response.getData());
                    assertEquals(response.getErrors(), "user tidak ditemukan");
                });
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_PEMBELI"})
    public void testGetUserLoggedIn() throws Exception{
        when(userService.findByEmail("testuser")).thenReturn(new User.Builder().email("testuser").status(Status.ROLE_PEMBELI).build());
        mockMvc.perform(get("/user/me").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                    });
                    assertNotNull(response.getData());
                    assertNull(response.getErrors());
                    assertEquals(response.getData().getEmail(),"testuser");
                    assertEquals(response.getData().getStatus(), Status.ROLE_PEMBELI);
                });
    }

    @Test
    public void testGetUserNotLoggedIn() throws Exception{
        mockMvc.perform(get("/user/me").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_PEMBELI"})
    public void testUpdateUserBalance() throws Exception{
        Map<String,Integer> request = new HashMap<>();
        request.put("saldo", 20000);
        mockMvc.perform(patch("/user/reduceBalance").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
                    assertEquals(response.getData(),"Ok");
                    assertNull(response.getErrors());
                });
    }

    @Test
    public void testSetUserBalanceNotLoggedIn() throws Exception{
        mockMvc.perform(patch("/user/reduceBalance").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_PEMBELI"})
    public void testSetUserBalanceNotValid() throws Exception{
        Map<String,Integer> request = new HashMap<>();
        request.put("saldo", -1);
        mockMvc.perform(patch("/user/reduceBalance").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
                    assertNull(response.getData());
                    assertNotNull(response.getErrors());
                    assertEquals(response.getErrors(), "Saldo tidak valid");
                });
    }

    @Test
    @WithMockUser(username = "user1@example.com", authorities = {"ROLE_PEMBELI"})
    public void testChangeUserRole() throws Exception {
        String newRole = "ROLE_PENJUAL";

        mockMvc.perform(post("/api/user/changeRole")
                        .param("newRole", newRole)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertEquals("Role updated successfully", response.getData());
                    assertNull(response.getErrors());
                });

        verify(userService, times(1)).changeUserRole("user1@example.com", Status.valueOf(newRole));
    }
}