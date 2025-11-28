package ss1.ong.datacenter.auth.login;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ss1.ong.datacenter.auth.login.dto.request.LoginDTO;
import ss1.ong.datacenter.auth.login.dto.request.LoginWithMfaDTO;
import ss1.ong.datacenter.auth.login.dto.response.LoginResponseDTO;
import ss1.ong.datacenter.auth.login.dto.response.TokenDTO;
import ss1.ong.datacenter.common.exceptions.NotFoundException;

/**
 * Controlador para el servicio de login
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * Endpoint para autenticar a un usuario.
     * Recibe credenciales y retorna un token JWT si son válidas.
     *
     * @param loginDTO objeto con el nombre de usuario y la contraseña
     * @return LoginResponseDTO con el token, nombre de usuario y rol
     */
    @Operation(summary = "Iniciar sesión",
            description = "Autentica a un usuario con su nombre de usuario y contraseña. Retorna un token JWT si las credenciales son válidas.",
            responses = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }


    /**
     * Endpoint para autenticar a un usuario usando mfa
     */
    @Operation(summary = "Endpoint para autenticar a un usuario usando mfa",
            description = "Endpoint para autenticar a un usuario usando mfa", responses = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/mfa")
    @ResponseStatus(HttpStatus.OK)
    public TokenDTO loginWithMFA(@RequestBody @Valid LoginWithMfaDTO loginWithMfaDTO) throws NotFoundException {
        return loginService.loginWithMfaDTO(loginWithMfaDTO);
    }

}
