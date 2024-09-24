package todo.dto.request.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ModifyToDoRequestDto {

    private Long listId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    @NotBlank
    private String priority;

    @NotBlank
    private String repeatInterval;

}

