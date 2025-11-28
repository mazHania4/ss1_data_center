package ss1.ong.datacenter.auth.mfaCodes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ss1.ong.datacenter.auth.mfaCodes.dto.request.EmailResendVerificationDTO;
import ss1.ong.datacenter.auth.users.AppUserService;
import ss1.ong.datacenter.common.exceptions.NotFoundException;

/**
 * Controlador REST para verificacion de correos electronicos
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-30-08
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MfaCodesController {

    private final AppUserService appUserService;
    private final MfaCodesService mfaCodesService;

    /**
     * Reenvia un codigo para la validacion del correo electronico o para la recuperacin en dos pasos
     * */
    @Operation(summary = "Reenvia el codigo de validacion de email",
            description = "Reenvia el codigo para que el usuario pueda validar su correo electronico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Codigo enviado"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @PostMapping("/send-code")
    @ResponseStatus(HttpStatus.OK)
    public void resedValidationEmailCode(@RequestBody @Valid EmailResendVerificationDTO emailResendVerificationDTO)
            throws NotFoundException {
        appUserService.resendUserCode(
                emailResendVerificationDTO.getUsername(),
                emailResendVerificationDTO.getEmail()
        );
    }

}
