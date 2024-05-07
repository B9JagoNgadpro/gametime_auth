package jagongadpro.autentikasi.dto;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class WebResponseTest {
    WebResponse<String> webResponse = WebResponse.<String>builder().data("Oke").errors("error").build();

    @Test
    void getterData(){
        assertEquals("Oke",webResponse.getData());
    }

    @Test
    void getterError(){
        assertEquals("error", webResponse.getErrors());
    }

    @Test
    void setterError(){
        webResponse.setErrors("new-error");
        assertEquals("new-error", webResponse.getErrors());
    }

    @Test
    void setterData(){
        webResponse.setData("newData");
        assertEquals("newData", webResponse.getData());
    }
    @Test
    void toStringTest(){
        assertNotNull(webResponse.toString());
    }
    @Test
    void testWebResponseBuilder(){
        WebResponse<String> response = WebResponse.<String>builder().data("OK").errors("error").build();
        assertEquals(response.getData(),"OK");
        assertEquals(response.getErrors(), "error");
    }
    @Test
    void testToString(){
        assertNotNull(WebResponse.<String>builder().data("Oke").errors("error").toString());

    }


}