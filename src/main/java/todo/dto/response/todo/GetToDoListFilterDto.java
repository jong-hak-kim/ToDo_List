package todo.dto.response.todo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GetToDoListFilterDto {
    private Long listId;
    private String title;
    private LocalDate date;
    private boolean completionStatus;

    public GetToDoListFilterDto(Long listId, String title, LocalDate date, boolean completionStatus) {
        this.listId = listId;
        this.title = title;
        this.date = date;
        this.completionStatus = completionStatus;
    }
}
