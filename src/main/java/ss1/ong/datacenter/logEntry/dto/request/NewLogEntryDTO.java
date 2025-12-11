package ss1.ong.datacenter.logEntry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ss1.ong.datacenter.logEntry.enums.LogLevelEnum;

public record NewLogEntryDTO(
    @NotNull Integer appUserId,
    @NotNull LogLevelEnum level,
    String relatedEntity,
    Integer relatedEntityId,
    @NotBlank String eventName,
    String details
) { }
