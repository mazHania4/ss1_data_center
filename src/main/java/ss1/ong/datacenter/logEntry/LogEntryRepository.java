package ss1.ong.datacenter.logEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ss1.ong.datacenter.logEntry.dto.response.UserLogStatsDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Integer> {

    @Query(value = """ 
            SELECT 
                u.id AS userId,
                u.name AS name,
                u.email AS email,
                u.role AS role,
                COUNT(l.id) AS total_logs
            FROM log_entry l
            INNER JOIN app_user u ON l.app_user_id = u.id
            WHERE l.level = 'INFO'
            GROUP BY u.id, u.name, u.email, u.role
            ORDER BY total_logs DESC
            LIMIT :limit """
            , nativeQuery = true)
    List<UserLogStatsDTO> findTopUsersByLogCount(Integer limit);

    @Query(value = """
        SELECT l.* FROM log_entry l
        WHERE l.deleted_at IS NULL
          AND (:appUserId IS NULL OR l.app_user_id = :appUserId)
          AND (:service IS NULL OR l.source_service = :service)
          AND (:level IS NULL OR l.level = :level)
          AND (CAST(:from AS timestamp) IS NULL OR l.occurred_at >= CAST(:from AS timestamp))
          AND (CAST(:to AS timestamp) IS NULL OR l.occurred_at <= CAST(:to AS timestamp))
        ORDER BY l.created_at
    """, nativeQuery = true)
    List<LogEntry> getLogEntriesFiltered(Integer appUserId, String service, String level, LocalDateTime from, LocalDateTime to);

}
