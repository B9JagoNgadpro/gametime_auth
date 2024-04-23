package jagongadpro.autentikasi.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
public class LoginUserRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}