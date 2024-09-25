package todo.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import todo.dto.response.ResponseDto;

@Data
@NoArgsConstructor
public class AdminSignInResponseDto extends ResponseDto {

    private String email;
    private String token;

    public AdminSignInResponseDto(String email, String token) {
        super("SU", "SUCCESS");
        this.email = email;
        this.token = token;
    }
}
