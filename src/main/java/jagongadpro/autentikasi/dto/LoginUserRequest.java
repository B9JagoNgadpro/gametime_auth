package jagongadpro.autentikasi.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
public class LoginUserRequest {
    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;

}