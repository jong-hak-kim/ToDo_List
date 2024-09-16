package todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AddToDoRequestDto {

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
