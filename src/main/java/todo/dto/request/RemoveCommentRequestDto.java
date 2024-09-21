package todo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveCommentRequestDto {

    @NotNull
    private Long CommentId;
}
