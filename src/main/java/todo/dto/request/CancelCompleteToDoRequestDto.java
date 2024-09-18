package todo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelCompleteToDoRequestDto {

    private Long listId;

}
