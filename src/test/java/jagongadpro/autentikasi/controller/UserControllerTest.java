package jagongadpro.autentikasi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jagongadpro.autentikasi.dto.PasswordDto;
import jagongadpro.autentikasi.dto.UserResponse;
import jagongadpro.autentikasi.dto.WebResponse;
import jagongadpro.autentikasi.enums.Status;
import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.springframework.security.test.context.support.WithMockUser;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @MockBean
    UserFacade userFacade;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
       void testResetPassword() throws Exception {
        String email = "test@example.com";
        WebResponse<String> responseMock = WebResponse.<String>builder().data("url sent").build();
        when(userFacade.resetPassword(any(HttpServletRequest.class), eq(email))).thenReturn(responseMock);
        mockMvc.perform(post("/user/password/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertEquals("url sent", response.getData() );
                });

    }
    @Test
       void testResetPasswordUserNotFound() throws Exception {
        String email = "test@example.com";
        when(userFacade.resetPassword(any(HttpServletRequest.class), eq(email))).thenThrow(new UserNotFoundException("Email tidak ditemukan"));
        mockMvc.perform(post("/user/password/resetPassword")
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                    assertEquals("Email tidak ditemukan", response.getErrors());
                });

    }

    @Test
       void showChangePasswordPageTestSuccess() throws  Exception{
        String token="token";
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("valid");
        when(userFacade.showChangePasswordPage(token)).thenReturn(responseEntity) ;
        mockMvc.perform(get("/user/password/changePassword").param("token", token))
                .andExpect(status().isOk())
                .andDo(result ->{
                    String response = result.getResponse().getContentAsString();
                    assertEquals("valid", response);
                } );

    }


    @Test
       void savePasswordSuccess() throws Exception{
        WebResponse<String> responseMock = WebResponse.<String>builder().data("password berhasil diganti").build();
        PasswordDto passwordDto = new PasswordDto("token", "newPassword");
        when(userFacade.savePassword(any(PasswordDto.class))).thenReturn(responseMock) ;
        mockMvc.perform(post("/user/password/savePassword").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordDto))).andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertEquals("password berhasil diganti", response.getData());
                });
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_PEMBELI"})
       void testGetUserLoggedIn() throws Exception{
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
       void testGetUserNotLoggedIn() throws Exception{
        mockMvc.perform(get("/user/me").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());

    }
    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_PEMBELI"})
       void testUpdateUserBalance() throws Exception{
        Map<String,Integer> request = new HashMap<>();
        request.put("saldo", 20000);
        mockMvc.perform(patch("/user/reduceBalance").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
                    assertEquals("Ok", response.getData());
                    assertNull(response.getErrors());
                });

    }
    @Test
       void testSetUserBalanceNotLoggedIn() throws Exception{
        mockMvc.perform(patch("/user/reduceBalance").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());

    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_PEMBELI"})
       void testSetUserBalanceNotValid() throws Exception{
        Map<String,Integer> request = new HashMap<>();
        request.put("saldo", -1);
        mockMvc.perform(patch("/user/reduceBalance").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
                    assertNull(response.getData());
                    assertNotNull(response.getErrors());
                    assertEquals("Saldo tidak valid", response.getErrors());
                });
    }




    @Test
    @WithMockUser(username = "user1@example.com", authorities = {"ROLE_PEMBELI"})
       void testChangeUserRole() throws Exception {
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