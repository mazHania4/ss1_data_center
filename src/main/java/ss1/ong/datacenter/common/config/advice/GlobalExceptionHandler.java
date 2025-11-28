package ss1.ong.datacenter.common.config.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ss1.ong.datacenter.common.exceptions.CustomRuntimeException;
import ss1.ong.datacenter.common.exceptions.DuplicateResourceException;
import ss1.ong.datacenter.common.exceptions.InvalidTokenException;
import ss1.ong.datacenter.common.exceptions.NotFoundException;
import ss1.ong.datacenter.common.models.dto.response.ErrorDTO;
import ss1.ong.datacenter.common.utils.MethodArgumentErrorExtractor;

/**
 * Clase para el manejo global de errores
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-27
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MethodArgumentErrorExtractor argumentErrorExtractor;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorDTO("Error inesperado en la Base de Datos.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ErrorDTO(argumentErrorExtractor.extractMethodArgumentError(ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorDTO("Autenticación fallida: El correo electrónico o la contraseña son incorrectos."
                + " Por favor, verifica tus credenciales e intenta de nuevo.");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return new ErrorDTO("No tienes autorización para acceder a este recurso. "
                + "Verifica que tengas los permisos adecuados o contacta al equipo de soporte si consideras que esto es un error.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleIllegalStateException(IllegalStateException ex) {
        return new ErrorDTO(ex.getMessage());
    }


    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleInvalidTokenException(InvalidTokenException ex) {
        return new ErrorDTO(
                """
                        No se pudo validar tu sesión correctamente.
                        Te recomendamos cerrar sesión e intentar ingresar nuevamente.
                        Si el problema persiste, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }


    @ExceptionHandler(CustomRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleCustomRuntimeException(CustomRuntimeException ex) {
        return new ErrorDTO(
                """
                        Ocurrió un error inesperado al procesar la información.
                        Por favor, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("Ocurrió un error inesperado: " + ex.getMessage());
    }

}
