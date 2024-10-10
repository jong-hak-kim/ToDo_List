package todo.dto.response.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GetOneToDoListResponseDto extends ResponseDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String priority;

    @NotBlank
    private LocalDate date;

    public GetOneToDoListResponseDto(String title, String content, String priority, LocalDate date) {
        super("SU", "SUCCESS");
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.date = date;
    }
}
