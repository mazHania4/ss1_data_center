package ss1.ong.datacenter.logEntry;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ss1.ong.datacenter.auth.users.AppUserService;
import ss1.ong.datacenter.common.exceptions.NotFoundException;
import ss1.ong.datacenter.logEntry.dto.request.NewLogEntryDTO;
import ss1.ong.datacenter.logEntry.dto.response.LogEntryResponseDTO;
import ss1.ong.datacenter.logEntry.dto.response.UserActivityResponseDTO;
import ss1.ong.datacenter.logEntry.dto.response.UserLogStatsDTO;
import ss1.ong.datacenter.logEntry.enums.LogLevelEnum;
import ss1.ong.datacenter.logEntry.enums.ServiceForLogEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LogEntryService {

    private  final LogEntryRepository logEntryRepository;
    private final LogEntryMapper logEntryMapper;
    private final AppUserService appUserService;

    public List<UserActivityResponseDTO> getTopUsersByActivity(Integer usersLimit) {
        List<UserLogStatsDTO> result = logEntryRepository.findTopUsersByLogCount(usersLimit);

        return result.stream()
                .map(r -> new UserActivityResponseDTO(
                        r.getUserId(),
                        r.getName(),
                        r.getEmail(),
                        r.getRole(),
                        r.getTotalLogs()
                ))
                .collect(Collectors.toList());
    }

    public List<LogEntryResponseDTO> getLogsFiltered(Integer appUserId, ServiceForLogEnum service, LogLevelEnum level, LocalDate startDate, LocalDate endDate) {
        return logEntryMapper.LogEntryToLogEntryResponseDTO(logEntryRepository.getLogEntriesFiltered(appUserId, service.name(), level.name(), startDate, endDate));
    }

    public LogEntryResponseDTO createLogEntry(@Valid NewLogEntryDTO newLogEntryDTO) throws NotFoundException {
        LogEntry entry = logEntryMapper.newLogEntryDTOToLogEntry(newLogEntryDTO);
        if (newLogEntryDTO.appUserId() != null) {
            entry.setAppUser(appUserService.getUserById(newLogEntryDTO.appUserId()));
        }
        entry.setOccurredAt(LocalDateTime.now());
        entry.setSourceService(ServiceForLogEnum.AYUDA_HUMANITARIA);
        return logEntryMapper.LogEntryToLogEntryResponseDTO(logEntryRepository.save(entry));
    }
}
