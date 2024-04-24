package jagongadpro.autentikasi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    User user = new User.Builder().email("abc@gmail.com").username("username").bio("bio").password("password").profileUrl("url").saldo(500000).build();
    @Test
    void getterEmail() {
        assertEquals("abc@gmail.com", user.getEmail());
    }
    @Test
    void getterUsename() {
        assertEquals("username", user.getUsernameReal());
    }
    @Test
    void getterBio() {
        assertEquals("bio", user.getBio());
    }
    @Test
    void getterPassword() {
        assertEquals("password", user.getPassword());
    }
    @Test
    void getterProfileUrl() {
        assertEquals("url", user.getProfileUrl());

    }
    @Test
    void getterSaldo() {
        assertEquals(500000, user.getSaldo());
    }
    @Test
    void getterStatusDefault() {
        assertEquals("PEMBELI", user.getStatus());

    }
    @Test
    void getterStatusUpdated() {
        User user = new User.Builder().status("PENJUAL").build();
        assertEquals("PENJUAL", user.getStatus());

    }
    @Test
    void implementUserDetail() {
        User user = new User.Builder().status("PENJUAL").build();
        assertEquals(true, user.isAccountNonExpired());
        assertEquals(true, user.isAccountNonLocked());
        assertEquals(true, user.isCredentialsNonExpired());
        assertEquals(true, user.isEnabled());

    }

    void setBioTest(){
        user.setBio("baru");
        assertEquals(user.getBio(), "baru");
    }
    void setPasswordTest(){
        user.setPassword("pwbaru");
        assertEquals(user.getBio(), "pwbaru");
    }
    void setStatusTest(){
        user.setStatus("PEMBELI");
        assertEquals(user.getBio(), "PEMBELI");
    }
    void setSaldoTest(){
        user.setSaldo(40000);
        assertEquals(user.getSaldo(), 40000);
    }
    void setUserNameTest(){
        user.setUsername("usernamebaru");
        assertEquals(user.getUsername(), "usernamebaru");
    }
    void setPasswordUrlTest(){
        user.setProfileUrl("urlbaru");
        assertEquals(user.getPassword(), "urlbaru");
    }




}
