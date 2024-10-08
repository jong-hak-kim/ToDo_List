package todo.dto.response.comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;
import todo.entity.Comment;

import java.util.List;

@Data
@NoArgsConstructor
public class GetCommentResponseDto extends ResponseDto {

    private List<GetCommentListFilterDto> comments;

    public GetCommentResponseDto(List<GetCommentListFilterDto> comments) {
        super("SU", "SUCCESS");
        this.comments = comments;
    }
}
