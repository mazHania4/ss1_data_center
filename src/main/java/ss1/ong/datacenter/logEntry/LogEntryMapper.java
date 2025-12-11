package ss1.ong.datacenter.logEntry;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ss1.ong.datacenter.logEntry.dto.request.NewLogEntryDTO;
import ss1.ong.datacenter.logEntry.dto.response.LogEntryResponseDTO;

import java.util.List;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LogEntryMapper {
    LogEntryResponseDTO LogEntryToLogEntryResponseDTO(LogEntry logEntry);
    List<LogEntryResponseDTO> LogEntryToLogEntryResponseDTO(List<LogEntry> logEntry);
    LogEntry newLogEntryDTOToLogEntry(NewLogEntryDTO newLogEntryDTO);

}
