package ss1.ong.datacenter.common.exceptions;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
public class InvalidTokenException extends CustomRuntimeException {

    public InvalidTokenException(String code, String message) {
        super(code, message);
    }

}