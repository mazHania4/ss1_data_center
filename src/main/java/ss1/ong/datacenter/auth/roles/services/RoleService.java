package ss1.ong.datacenter.auth.roles.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import ss1.ong.datacenter.auth.users.enums.RolesEnum;
import ss1.ong.datacenter.common.exceptions.NotFoundException;

/**
 * Servicio que gestiona operaciones relacionadas con los roles de usuario.
 *
 * Utiliza el enum {@link RolesEnum} como fuente central de definición de roles.
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Service
public class RoleService {

    /**
     * Obtiene la lista de todos los roles definidos en el sistema, excluyendo el
     * rol {@code RolesEnum#PARTICIPANT}.
     *
     */
    public List<RolesEnum> findAllRoles() {
        return Arrays.stream(RolesEnum.values())
                .toList();
    }

    /**
     * Busca un rol del sistema basado en su etiqueta descriptiva.
     *
     */
    public RolesEnum findRoleByLabel(String label) throws NotFoundException {
        return Arrays.stream(RolesEnum.values())
                .filter(role -> role.getRoleLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No se encontró un rol con la etiqueta: " + label));
    }
}
