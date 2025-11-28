package ss1.ong.datacenter.auth.login.dto.response;

import lombok.Value;
import ss1.ong.datacenter.auth.users.dto.response.UserDTO;

/**
 * DTO que representa la respuesta al iniciar sesión.
 * Contiene el nombre del usuario, su rol y el token JWT generado.
 *
 * Esta clase es inmutable y utiliza Lombok para generar automáticamente
 * constructor, getters, equals, hashCode y toString.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Value
public class LoginResponseDTO {

    UserDTO user;
    String token;
}
