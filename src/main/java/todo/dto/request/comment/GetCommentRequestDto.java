package todo.dto.request.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCommentRequestDto {

    @NotNull
    private Long listId;
}
