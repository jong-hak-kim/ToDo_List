package todo.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveUserToDoRequestDto {

    @NotNull
    private Long listId;

    @NotBlank
    private String reason;

}
