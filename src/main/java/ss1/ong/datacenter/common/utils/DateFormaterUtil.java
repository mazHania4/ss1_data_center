package ss1.ong.datacenter.common.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para formateo de fechas en formato local.
 * 
 * Esta clase proporciona métodos para convertir objetos {@link LocalDate} a
 * cadenas con un formato legible para usuarios que utilizan el orden
 * día/mes/año.
 * 
 * Puede ser utilizada por servicios o controladores que necesiten presentar
 * fechas
 * en interfaces orientadas al usuario.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Component
public class DateFormaterUtil {

    /**
     * Convierte un {@link Instant} a una cadena con formato local "dd/MM/yyyy"
     * utilizando la zona horaria del sistema.
     * 
     * @param instant el instante en UTC a convertir
     * @return la fecha formateada como cadena en formato "dd/MM/yyyy"
     */
    public String parseToLocalFormat(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    public Instant parseToInstant(LocalDate localDate){
        if (localDate == null) {
            return null;
        }
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public Instant parseToInstant(LocalDateTime localDateTime){
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
