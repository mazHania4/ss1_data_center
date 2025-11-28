package ss1.ong.datacenter.auth.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ss1.ong.datacenter.auth.users.dto.request.CreateUserByAdminDTO;
import ss1.ong.datacenter.auth.users.dto.request.CreateUserDTO;
import ss1.ong.datacenter.auth.users.dto.request.RecoveryPasswordDTO;
import ss1.ong.datacenter.auth.users.dto.request.UpdateUserByAdminDTO;
import ss1.ong.datacenter.auth.users.dto.response.ExistUsernameDTO;
import ss1.ong.datacenter.auth.users.dto.response.UserDTO;
import ss1.ong.datacenter.common.exceptions.NotFoundException;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-28-08
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;

    /**
     * Endpoint para que un usuario se registre
     */
    @Operation(summary = "Sign in", description = "Registra a un usuario en el sistema",
            responses = {
            @ApiResponse(responseCode = "200", description = "Se envio el correo de confirmacion"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO signIn(@RequestBody @Valid CreateUserDTO createUserDTO) {
        AppUser createdUser = appUserService.createUser(createUserDTO);
        return appUserMapper.appUserToUserDto(createdUser);
    }

    /**
     * Endpoint para que un usuario active o desactive la verificacion en dos pasos
     */
    @Operation(summary = "Endpoint para que un usuario active o desactive la verificacion en dos pasos",
            description = "Endpoint para que un usuario active o desactive la verificacion en dos pasos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se envio el correo de confirmacion"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @PostMapping("/change-mfa2")
    @ResponseStatus(HttpStatus.OK)
    public void changeMFA() throws NotFoundException {
        appUserService.change2StepVerification();
    }

    /**
     * Endpoint para que un usuario pueda recuperar su contrasena
     */
    @Operation(summary = "recovery password", description = "Registra a un usuario en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se envio el correo de confirmacion"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @PostMapping("/password-recovery")
    @ResponseStatus(HttpStatus.OK)
    public void passwordRecovery(@RequestBody @Valid RecoveryPasswordDTO recoveryPasswordDTO) throws NotFoundException {
        appUserService.recoveryPassword(recoveryPasswordDTO);
    }


    /**
     * Obtener a un usuario por su username
     * Opcion disponible para el administrador
     **/
    @Operation(summary = "Sign in", description = "Registra a un usuario en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exitoso"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @GetMapping("/user/get/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getByUsername(@PathVariable String username) throws NotFoundException {
        AppUser user = appUserService.getUserByUsername(username);
        return appUserMapper.appUserToUserDto(user);
    }

    /**
     * Verificar si un username existe
     **/
    @Operation(summary = "Exists username", description = "Registra a un usuario en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exitoso"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @GetMapping("/user/exist/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ExistUsernameDTO existUsername(@PathVariable String username) {
        return new ExistUsernameDTO(appUserService.existUserByUsername(username));
    }

    /**
     * Enpoint para obtener el perfil del usuario
     * */
    @Operation(summary = "get Profile", description =
            "obtienen el perfil del usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "usuario activado"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @GetMapping("/users/profile")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public UserDTO getProfile() throws NotFoundException {
        return appUserService.getProfile();
    }

    /**
     * Endpoint para que un usuario se actualice
     */
    @Operation(summary = "update user", description = "Actualiza a un usuario en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se envio el correo de confirmacion"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable("userId") Integer userId, @RequestBody @Valid UpdateUserByAdminDTO updateUserByAdminDTO) throws NotFoundException {
        return appUserService.updateAppUser(userId, updateUserByAdminDTO);
    }

    /**
     * Enpoint para obtener todos los usuarios
     * */
    @Operation(summary = "get Profile", description =
            "obtienen el perfil del usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "usuario activado"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @GetMapping("/users/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllAppUsers() throws NotFoundException {
        return appUserService.getAllAppUsers();
    }

    /**
     * Enpoint para crear a un usuario
     * */
    @Operation(summary = "create user", description = "crear usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "usuario creado"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createUserByAdmin(@RequestBody @Valid CreateUserByAdminDTO createUserByAdminDTO)
            throws NotFoundException {
        AppUser createdUser = appUserService.createUserByAdmin(createUserByAdminDTO);
        return appUserMapper.appUserToUserDto(createdUser);
    }


}
