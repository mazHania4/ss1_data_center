package ss1.ong.datacenter.auth.users.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateUserDTO {

    @NotBlank(message = "Por favor, ingresa un nombre de usuario.")
    @Size(max = 20, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String username;

    @JsonAlias("password")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 16, message = "La contraseña debe tener entre 8 y 16 caracteres")
    String passwordHash;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Size(min = 8, max = 16, message = "La contraseña debe tener al menos 8 caracteres")
    String phoneNumber;

    @NotBlank(message = "El nombre no puede ser vacio")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String name;

    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String lastname;

    @NotBlank(message = "El email no puede ser vacio")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String email;

}
