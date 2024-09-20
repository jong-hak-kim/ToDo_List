package todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyCommentRequestDto {

    @NotNull
    private Long toDoListId;

    @NotNull
    private Long commentId;

    @NotBlank
    private String content;
}
