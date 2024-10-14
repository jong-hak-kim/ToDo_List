package todo.dto.response.todo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetUserListFilterDto {
    private String email;

    public GetUserListFilterDto(String email) {
        this.email = email;
    }
}
