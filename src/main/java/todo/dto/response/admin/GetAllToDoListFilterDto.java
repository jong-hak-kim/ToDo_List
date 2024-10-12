package todo.dto.response.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetAllToDoListFilterDto {

    @NotNull
    private Long listId;

    @NotBlank
    private String title;

    @NotBlank
    private String email;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDate date;

    public GetAllToDoListFilterDto(Long listId, String title, String email, LocalDateTime creationDate, LocalDate date) {
        this.listId = listId;
        this.title = title;
        this.email = email;
        this.creationDate = creationDate;
        this.date = date;
    }
}
