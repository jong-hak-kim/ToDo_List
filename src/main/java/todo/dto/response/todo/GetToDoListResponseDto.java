package todo.dto.response.todo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetToDoListResponseDto extends ResponseDto {

    private List<GetToDoListFilterDto> filterToDoList;

    public GetToDoListResponseDto(List<GetToDoListFilterDto> filterToDoList) {
        super("SU", "SUCCESS");
        this.filterToDoList = filterToDoList;
    }
}
