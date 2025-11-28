package ss1.ong.datacenter.auth.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ss1.ong.datacenter.auth.users.enums.RolesEnum;
import ss1.ong.datacenter.auth.users.enums.StatusUserEnum;

@Value
public class CreateUserByAdminDTO {

    @NotBlank(message = "Por favor, ingresa un nombre de usuario.")
    @Size(max = 20, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String username;

    @NotBlank(message = "El email no puede ser vacio")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String email;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Size(min = 8, max = 16, message = "La contraseña debe tener al menos 8 caracteres")
    String phoneNumber;

    @NotBlank(message = "El nombre no puede ser vacio")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String name;

    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String lastname;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 16, message = "La contraseña debe tener al menos 8 caracteres")
    String password;

    RolesEnum role;

    StatusUserEnum status;

    @NotNull(message = "Se debe especificar si es necesaria la verificacion en dos pasos")
    Boolean mfaActivated;
}
