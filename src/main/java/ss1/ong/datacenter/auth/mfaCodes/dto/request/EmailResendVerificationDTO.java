package ss1.ong.datacenter.auth.mfaCodes.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class EmailResendVerificationDTO {
    @NotBlank(message = "El email tiene que estar presente")
    String email;
    @NotBlank(message = "El username tiene que estar presente")
    String username;
}
