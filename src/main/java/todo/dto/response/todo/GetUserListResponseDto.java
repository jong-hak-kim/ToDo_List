package todo.dto.response.todo;

import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
public class GetUserListResponseDto extends ResponseDto {

    List<GetUserListFilterDto> users;

    public GetUserListResponseDto(List<GetUserListFilterDto> users) {
        super("SU", "SUCCESS");
        this.users = users;
    }
}
