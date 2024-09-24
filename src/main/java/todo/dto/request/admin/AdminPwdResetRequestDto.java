package todo.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminPwdResetRequestDto {

    @NotBlank
    private String email;

}
