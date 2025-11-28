package ss1.ong.datacenter.auth.users;

import org.mapstruct.*;
import ss1.ong.datacenter.auth.users.dto.request.CreateUserByAdminDTO;
import ss1.ong.datacenter.auth.users.dto.request.CreateUserDTO;
import ss1.ong.datacenter.auth.users.dto.request.UpdateUserByAdminDTO;
import ss1.ong.datacenter.auth.users.dto.response.UserDTO;

import java.util.List;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AppUserMapper {
    public AppUser createUserDtoToAppUser(CreateUserDTO createUserDTO);
    public UserDTO appUserToUserDto(AppUser appUser);
    public List<UserDTO> appUsersToUserDtos(List<AppUser> appUsers);

    public AppUser createUserByAdminDtoToAppUser(CreateUserByAdminDTO createUserByAdminDTO);

    // update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAppUserFromDto(UpdateUserByAdminDTO dto, @MappingTarget AppUser entity);

}
