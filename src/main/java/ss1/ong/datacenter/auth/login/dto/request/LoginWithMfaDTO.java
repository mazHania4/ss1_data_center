package ss1.ong.datacenter.auth.login.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class LoginWithMfaDTO {
    @NotNull(message = "El id del usuario es requerido")
    Integer userId;

    @NotBlank(message = "El codigo no puede estar vacio")
    String code;
}
