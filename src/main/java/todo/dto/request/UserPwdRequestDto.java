package todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class UserPwdRequestDto {

    @NotBlank
    @Length(min = 8, max = 20)
    private String originalPwd;

    @NotBlank
    @Length(min = 8, max = 20)
    private String newPwd;
}
