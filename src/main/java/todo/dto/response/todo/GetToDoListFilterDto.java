package todo.dto.response.todo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetToDoListFilterDto {
    private Long listId;
    private String title;
    private boolean completionStatus;

    public GetToDoListFilterDto(Long listId, String title, boolean completionStatus) {
        this.listId = listId;
        this.title = title;
        this.completionStatus = completionStatus;
    }
}
