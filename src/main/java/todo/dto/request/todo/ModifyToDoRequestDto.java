package todo.dto.request.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ModifyToDoRequestDto {

    private Long listId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotBlank
    private String priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repeatEndDate;

}

