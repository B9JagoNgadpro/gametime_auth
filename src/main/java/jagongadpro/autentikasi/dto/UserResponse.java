package jagongadpro.autentikasi.dto;

import jagongadpro.autentikasi.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserResponse {
    String email;
    String username;
    Integer saldo;
    Status status;
}
