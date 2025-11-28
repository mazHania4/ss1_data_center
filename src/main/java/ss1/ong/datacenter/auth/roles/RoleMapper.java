package ss1.ong.datacenter.auth.roles;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ss1.ong.datacenter.auth.roles.models.dto.response.RoleDTO;
import ss1.ong.datacenter.auth.users.enums.RolesEnum;

/**
 * Mapper encargado de convertir elementos del enum {@link RolesEnum} a objetos
 * {@link RoleDTO}.
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-04
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "roleLabel", target = "label")
    public RoleDTO rolesEnumtoRoleDTO(RolesEnum role);

    public List<RoleDTO> rolesEnumListtoRoleDTOList(List<RolesEnum> roles);

}
