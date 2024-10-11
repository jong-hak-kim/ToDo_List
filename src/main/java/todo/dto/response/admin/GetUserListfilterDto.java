package todo.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetUserListfilterDto {

    @NotBlank
    private String email;

    @NotBlank
    private String role;

    @NotBlank
    private String phoneNumber;

    private LocalDateTime deactivationDate;

    @NotNull
    @JsonProperty("isActive")
    private boolean isActive;

    public GetUserListfilterDto(String email, String role, String phoneNumber, LocalDateTime deactivationDate, boolean isActive) {
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.deactivationDate = deactivationDate;
        this.isActive = isActive;
    }
}
