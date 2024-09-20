package todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCommentRequestDto {

    @NotNull
    private Long toDoListId;

    private Long parentCommentId;

    @NotBlank
    private String content;

}
