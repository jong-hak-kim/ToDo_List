package todo.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveUserRequestDto {

    @NotBlank
    private String email;
}
