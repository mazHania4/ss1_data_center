package ss1.ong.datacenter.logEntry.dto.response;

public record UserActivityResponseDTO (
        Long userId,
        String name,
        String email,
        String role,
        Long totalLogs
){ }
