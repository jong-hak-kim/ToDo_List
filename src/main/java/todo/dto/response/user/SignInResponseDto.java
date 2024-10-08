package todo.dto.response.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;
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
