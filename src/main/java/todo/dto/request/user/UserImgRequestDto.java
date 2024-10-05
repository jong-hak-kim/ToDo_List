package todo.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserImgRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private MultipartFile image;
}
