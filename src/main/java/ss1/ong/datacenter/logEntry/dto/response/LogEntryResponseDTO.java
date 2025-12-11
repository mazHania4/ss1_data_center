package ss1.ong.datacenter.logEntry.dto.response;

import ss1.ong.datacenter.auth.users.AppUser;
import ss1.ong.datacenter.auth.users.dto.response.SimpleUserDTO;
import ss1.ong.datacenter.logEntry.enums.LogLevelEnum;
import ss1.ong.datacenter.logEntry.enums.ServiceForLogEnum;

import java.time.LocalDateTime;

public record LogEntryResponseDTO(
        LocalDateTime occurredAt,
        ServiceForLogEnum sourceService,
        LogLevelEnum level,
        String eventName,
        SimpleUserDTO appUser,
        String details
) { }
