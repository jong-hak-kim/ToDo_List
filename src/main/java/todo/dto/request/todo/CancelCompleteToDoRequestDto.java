package todo.dto.request.todo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelCompleteToDoRequestDto {

    private Long listId;

}
