package todo.dto.response.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

@Data
@NoArgsConstructor
public class GetUserImgResponseDto extends ResponseDto {

    @NotBlank
    private String profileImg;

    public GetUserImgResponseDto(String profileImg) {
        super("SU","SUCCESS");
        this.profileImg = profileImg;
    }
}
