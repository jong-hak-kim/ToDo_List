package todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeUserImgRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String image;
}
