package todo.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPwdRequestDto {

    @NotBlank
    private String email;

    public ResetPwdRequestDto(String email) {
        this.email = email;
    }
}
