package ss1.ong.datacenter.auth.roles.models.dto.response;

import lombok.Value;

/**
 * DTO utilizado para exponer los roles disponibles en el sistema a través de la
 * API.
 * Contiene únicamente la etiqueta legible del rol, destinada a interfaces de
 * usuario.
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Value
public class RoleDTO {

    String label;
}
