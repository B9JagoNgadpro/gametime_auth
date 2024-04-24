package jagongadpro.autentikasi.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    String token;
    Long expiredIn;
}
