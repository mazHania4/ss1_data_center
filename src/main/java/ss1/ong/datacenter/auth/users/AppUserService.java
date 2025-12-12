package ss1.ong.datacenter.auth.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ss1.ong.datacenter.auth.mfaCodes.MfaCodes;
import ss1.ong.datacenter.auth.mfaCodes.MfaCodesService;
import ss1.ong.datacenter.auth.users.dto.request.CreateUserByAdminDTO;
import ss1.ong.datacenter.auth.users.dto.request.CreateUserDTO;
import ss1.ong.datacenter.auth.users.dto.request.RecoveryPasswordDTO;
import ss1.ong.datacenter.auth.users.dto.request.UpdateUserByAdminDTO;
import ss1.ong.datacenter.auth.users.dto.response.UserDTO;
import ss1.ong.datacenter.auth.users.enums.RolesEnum;
import ss1.ong.datacenter.auth.users.enums.StatusUserEnum;
import ss1.ong.datacenter.common.exceptions.CustomException;
import ss1.ong.datacenter.common.exceptions.DuplicateResourceException;
import ss1.ong.datacenter.common.exceptions.NotFoundException;

import java.util.List;

/**
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserMapper appUserMapper;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MfaCodesService mfaCodesService;

    /**
    * registro de  un usuario
    * */
    public AppUser createUser(CreateUserDTO createUserDTO) throws CustomException {
        if(appUserRepository.existsByUsername(createUserDTO.getUsername())){
            throw new DuplicateResourceException("El nombre de usuario ya esta en uso, elige uno diferente");
        }

        String encryptedPassword = passwordEncoder.encode(createUserDTO.getPassword());
        AppUser user = appUserMapper.createUserDtoToAppUser(createUserDTO);
        if( user.getRole() == RolesEnum.ADMIN){
            throw new CustomException("No se pudo especificar el rol de usuario adecuado");
        }
        user.setPassword(encryptedPassword);
        user.setStatus(StatusUserEnum.ACTIVE);
        return appUserRepository.save(user);
    }

    public void change2StepVerification() throws NotFoundException {
        AppUser appUser = this.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        appUser.setMfaActivated(!appUser.getMfaActivated());
        appUserRepository.save(appUser);
    }

    public void resendUserCode(String username) throws NotFoundException {
        AppUser appUser = appUserRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("No se encontro ese username registrado"));
        String verificationCode = mfaCodesService.getVerificationCode();
        mfaCodesService.saveVerification(verificationCode, appUser);
        mfaCodesService.sendVerificationEmail(appUser, verificationCode);
    }

    public void resendUserCode(String username, String email) throws NotFoundException {
        AppUser appUser = appUserRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("No se encontro ese username registrado"));

        if(!appUser.getEmail().equals(email)){
            throw new BadCredentialsException("El correo electronico no concuerda con el username");
        }
        String verificationCode = mfaCodesService.getVerificationCode();
        mfaCodesService.saveVerification(verificationCode, appUser);
        mfaCodesService.sendVerificationEmail(appUser, verificationCode);
    }

    public void recoveryPassword(RecoveryPasswordDTO recoveryPasswordDTO) throws NotFoundException {
        MfaCodes verification = mfaCodesService.verificateCode(recoveryPasswordDTO.getCode(), recoveryPasswordDTO.getUsername());
        AppUser user = verification.getAppUser();
        String encryptedPassword = passwordEncoder.encode(recoveryPasswordDTO.getPassword());
        user.setPassword(encryptedPassword);
        appUserRepository.save(user);
    }

    public AppUser getUserByUsername(String username) throws NotFoundException {
        return appUserRepository.findUserByUsername(username).orElseThrow(
                () ->    new NotFoundException("No se encontró un usuario con el username proporcionado.")
        );
    }

    public AppUser getUserByEmail(String email) throws NotFoundException {
        return appUserRepository.findUserByEmail(email).orElseThrow(
                () ->    new NotFoundException("No se encontró un usuario con el username proporcionado.")
        );
    }

    public boolean existUserByUsername(String username){
        return appUserRepository.existsByUsername(username);
    }

    public AppUser getUserById(Integer id) throws NotFoundException {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontro al usuario con ese id"));
    }

    public UserDTO getProfile() throws NotFoundException {
        AppUser appUser =appUserRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("No se encontro al participante"));
        return appUserMapper.appUserToUserDto(appUser);
    }

    public UserDTO updateAppUser(Integer userId, UpdateUserByAdminDTO updateUserByAdminDTO) throws NotFoundException {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No se encontró el usuario con id:" + userId));

        appUserMapper.updateAppUserFromDto(updateUserByAdminDTO, user);

        if (updateUserByAdminDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserByAdminDTO.getPassword()));
        }

        return appUserMapper.appUserToUserDto(appUserRepository.save(user));
    }

    public List<UserDTO> getAllAppUsers(){
        List<AppUser> users = appUserRepository.findAll();
        return appUserMapper.appUsersToUserDtos(users);
    }

    public AppUser createUserByAdmin(CreateUserByAdminDTO createUserByAdminDTO){
        if(appUserRepository.existsByUsername(createUserByAdminDTO.getUsername())){
            throw new DuplicateResourceException("El nombre de usuario ya esta en uso, elige uno diferente");
        }
        if(appUserRepository.existsByEmail(createUserByAdminDTO.getEmail())){
            throw new DuplicateResourceException("El correo electronico ya esta registrado, intenta con uno diferente");
        }
        String encryptedPassword = passwordEncoder.encode(createUserByAdminDTO.getPassword());
        AppUser user = appUserMapper.createUserByAdminDtoToAppUser(createUserByAdminDTO);
        user.setPassword(encryptedPassword);
        return appUserRepository.save(user);
    }
}
