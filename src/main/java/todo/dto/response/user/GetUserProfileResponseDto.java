package todo.dto.response.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

@Data
@NoArgsConstructor
public class GetUserProfileResponseDto extends ResponseDto {

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String profileImg;

    public GetUserProfileResponseDto(String email, String phoneNumber, String profileImg) {
        super("SU", "SUCCESS");
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImg = profileImg;
    }
}
