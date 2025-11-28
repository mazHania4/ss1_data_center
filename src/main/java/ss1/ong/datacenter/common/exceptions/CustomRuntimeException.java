package ss1.ong.datacenter.common.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Getter
@Setter
public class CustomRuntimeException extends RuntimeException {

    private String code;

    public CustomRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }
}