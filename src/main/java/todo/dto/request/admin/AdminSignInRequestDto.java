package todo.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class AdminSignInRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

}
