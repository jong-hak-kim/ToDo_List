package todo.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import todo.entity.User;

@Data
@NoArgsConstructor
public class SignInResponseDto extends ResponseDto {
    private String email;
    private String token;

    public SignInResponseDto(User user, String token) {
        super("SU", "Success");
        this.email = user.getEmail();
        this.token = token;
    }
}