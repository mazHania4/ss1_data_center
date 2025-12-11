package ss1.ong.datacenter.logEntry;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ss1.ong.datacenter.common.exceptions.NotFoundException;
import ss1.ong.datacenter.logEntry.dto.request.NewLogEntryDTO;
import ss1.ong.datacenter.logEntry.dto.response.LogEntryResponseDTO;
import ss1.ong.datacenter.logEntry.dto.response.UserActivityResponseDTO;
import ss1.ong.datacenter.logEntry.enums.LogLevelEnum;
import ss1.ong.datacenter.logEntry.enums.ServiceForLogEnum;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dc/logs")
@AllArgsConstructor
public class LogEntryController {

    private final LogEntryService logEntryService;

    /**
     * Obtiene el top de usuarios según su actividad en el sistema
     */
    @Operation(summary = "Obtiene el top de usuarios según su actividad en el sistema", description = "Obtiene el top de usuarios según su actividad en el sistema, se puede definir la cantidad de usuarios a incluir")
    @GetMapping("/userActivity")
    @ResponseStatus(HttpStatus.OK)
    public List<UserActivityResponseDTO> getTopUsersByActivity(
            @RequestParam(value="usersLimit", required = false, defaultValue = "5") Integer usersLimit
    ) {
        return logEntryService.getTopUsersByActivity(usersLimit);
    }


    /**
     * Obtiene un historial de comisiones con filtros opcionales
     *
     */
    @Operation(summary = "Obtiene un historial de comisiones con filtros opcionales", description = "Obtiene un listado de sus comisiones según los filtros opcionales")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<LogEntryResponseDTO> getLogsFiltered(
            @RequestParam(value="appUserId", required = false) Integer appUserId,
            @RequestParam(value="service", required = false) ServiceForLogEnum service,
            @RequestParam(value="level", required = false) LogLevelEnum level,
            @RequestParam(value="startDate", required = false) LocalDate startDate,
            @RequestParam(value="endDate", required = false) LocalDate endDate
    ) {
        return logEntryService.getLogsFiltered(appUserId, service, level, startDate, endDate);
    }


    /**
     * Agrega un nuevo registro
     *
     */
    @Operation(summary = "Agrega un nuevo registro ", responses = {
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Registro con los mismos datos ya registrada")
    })
    @PostMapping("/hh")
    @ResponseStatus(HttpStatus.CREATED)
    public LogEntryResponseDTO createLogEntry(@RequestBody @Valid NewLogEntryDTO newLogEntryDTO) throws NotFoundException {
        return logEntryService.createLogEntry(newLogEntryDTO);
    }

}
