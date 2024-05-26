package jagongadpro.autentikasi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    String token;
    Long expiredIn;
}
