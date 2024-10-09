package todo.dto.response.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetCommentListFilterDto {

    @NotNull
    private Long commentId;

    private Long parentCommentId;

    @NotBlank
    private String email;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime creationDate;

    public GetCommentListFilterDto(Long commentId, Long parentCommentId, String email, String content, LocalDateTime creationDate) {
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.email = email;
        this.content = content;
        this.creationDate = creationDate;
    }
}
