package ss1.ong.datacenter.auth.login;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ss1.ong.datacenter.auth.jwt.JwtGeneratorService;
import ss1.ong.datacenter.auth.login.dto.request.LoginDTO;
import ss1.ong.datacenter.auth.login.dto.request.LoginWithMfaDTO;
import ss1.ong.datacenter.auth.login.dto.response.LoginResponseDTO;
import ss1.ong.datacenter.auth.login.dto.response.TokenDTO;
import ss1.ong.datacenter.auth.mfaCodes.MfaCodes;
import ss1.ong.datacenter.auth.mfaCodes.MfaCodesRepository;
import ss1.ong.datacenter.auth.users.AppUser;
import ss1.ong.datacenter.auth.users.AppUserMapper;
import ss1.ong.datacenter.auth.users.AppUserService;
import ss1.ong.datacenter.auth.users.enums.StatusUserEnum;
import ss1.ong.datacenter.common.exceptions.NotFoundException;

import java.time.LocalDateTime;

/**
 * Servicio encargado de manejar el proceso de autenticación de usuarios.
 * Verifica las credenciales del usuario y, si son válidas, genera un token JWT.
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoginService {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGeneratorService jwtGeneratorService;
    private final AppUserMapper appUserMapper;
    private final MfaCodesRepository mfaCodesRepository;

    private final static String MAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$";

    /**
     * Autentica a un usuario en el sistema utilizando su correo o nombre de usuario
     * y contraseña.
     * Si las credenciales son válidas, genera y retorna un token JWT junto con
     * información del usuario.
     *
     */
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            AppUser user = appUserService.getUserByUsername(loginDTO.getUsername());
            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
                throw new BadCredentialsException("");
            }
            if (user.getStatus() != StatusUserEnum.ACTIVE) {
                switch (user.getStatus()) {
                    case StatusUserEnum.INACTIVE ->
                            throw new BadCredentialsException("Estas inactivo");
                    case StatusUserEnum.SUSPENDED ->
                            throw new BadCredentialsException("Has sido suspendido, no puedes acceder al sistema");
                }
            }
            if(user.getMfaActivated() != null && user.getMfaActivated()){
                appUserService.resendUserCode(user.getUsername());
                return new LoginResponseDTO(appUserMapper.appUserToUserDto(user), "");
            } else {
                String token = jwtGeneratorService.generateToken(user);
                return new LoginResponseDTO(appUserMapper.appUserToUserDto(user), token);
            }
        } catch (NotFoundException e) {
            throw new BadCredentialsException("");
        }
    }

    public TokenDTO loginWithMfaDTO(LoginWithMfaDTO loginWithMfaDTO) throws NotFoundException {
        MfaCodes mfaCodes = mfaCodesRepository.findByCodeAndAppUser_Id(
                loginWithMfaDTO.getCode(), loginWithMfaDTO.getUserId()
        ).orElseThrow(()-> new BadCredentialsException("Codigo incorrecto"));
        if(mfaCodes.getIsUsed() || mfaCodes.getExpirationTime().isBefore(LocalDateTime.now())){
            throw new BadCredentialsException("El codigo ya vencio o ya fue usuado");
        }
        AppUser appUser = appUserService.getUserById(loginWithMfaDTO.getUserId());
        mfaCodes.setIsUsed(true);
        mfaCodesRepository.save(mfaCodes);
        String token = jwtGeneratorService.generateToken(appUser);
        return new TokenDTO(token);
    }
}
