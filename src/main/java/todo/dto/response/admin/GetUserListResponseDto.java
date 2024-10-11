package todo.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
public class GetUserListResponseDto extends ResponseDto {

    List<GetUserListfilterDto> users;

    public GetUserListResponseDto(List<GetUserListfilterDto> users) {
        super("SU", "SUCCESS");
        this.users = users;
    }
}
