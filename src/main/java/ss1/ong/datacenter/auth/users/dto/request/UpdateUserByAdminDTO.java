package ss1.ong.datacenter.auth.users.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ss1.ong.datacenter.auth.users.enums.RolesEnum;
import ss1.ong.datacenter.auth.users.enums.StatusUserEnum;

@Value
public class UpdateUserByAdminDTO {

    @NotNull(message = "Se requiere un identificador de usuario")
    Integer id;
    String username;
    String email;
    String name;
    String lastname;
    String password;
    RolesEnum role;
    StatusUserEnum status;
    Boolean authentication;

}
