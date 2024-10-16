package todo.dto.request.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserProfileModifyRequestDto {

    private String phoneNumber;

    private MultipartFile profileImg;

    private String currentPassword;

    private String newPassword;

}
