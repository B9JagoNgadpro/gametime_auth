package jagongadpro.autentikasi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    @NotNull(message = "token tidak boleh kosong")
    @NotBlank (message = "token tidak boleh kosong")
    private  String token;

    @NotNull(message = "password tidak boleh kosong")
    @NotBlank(message = "password tidak boleh kosong")
    private String newPassword;
}
