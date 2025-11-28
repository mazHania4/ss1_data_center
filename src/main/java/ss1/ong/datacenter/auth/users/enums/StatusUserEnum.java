package ss1.ong.datacenter.auth.users.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que define los estados de las cuentas de usuario en el sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-04-09
 */
@AllArgsConstructor
@Getter
public enum StatusUserEnum {
    ACTIVE,
    SUSPENDED,
    INACTIVE;
}
