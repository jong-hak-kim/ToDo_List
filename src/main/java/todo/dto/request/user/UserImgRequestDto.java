package todo.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserImgRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String image;
}
