package todo.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
public class GetAllToDoListResponseDto extends ResponseDto {

    List<GetAllToDoListFilterDto> toDoLists;

    public GetAllToDoListResponseDto(List<GetAllToDoListFilterDto> toDoLists) {
        super("SU", "SUCCESS");
        this.toDoLists = toDoLists;
    }
}
