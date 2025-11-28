package ss1.ong.datacenter.auth.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CompleteRegistrationDTO {
    @NotBlank(message = "El email no puede ser vacio")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String email;

    @NotBlank(message = "El codigo es necesario")
    @Size(max = 6, min = 6, message = "El codigo debe de ser de 6 caracteres")
    String code;
}
