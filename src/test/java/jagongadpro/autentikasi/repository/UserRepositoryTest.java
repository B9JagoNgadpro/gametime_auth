package jagongadpro.autentikasi.repository;

import jagongadpro.autentikasi.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testByEmailFound(){
        User user = new User.Builder().email("abc@gmail.com").build();
        userRepository.save(user);

        User getUser = userRepository.findByEmail("abc@gmail.com").orElse(null);
        assertNotNull(getUser);
    }
    @Test
    void testByEmailNotFound(){
        User getUser = userRepository.findByEmail("abc@gmail.com").orElse(null);
        assertNull(getUser);
    }

}