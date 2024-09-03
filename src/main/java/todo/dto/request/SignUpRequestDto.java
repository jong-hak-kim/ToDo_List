package todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor

public class SignUpRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    private String profileImg;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 유효하지 않습니다.")
    private String phoneNumber;
}
