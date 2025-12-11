package ss1.ong.datacenter.logEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ss1.ong.datacenter.logEntry.dto.response.UserLogStatsDTO;

import java.time.LocalDate;
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
            ORDER BY totalLogs DESC
            LIMIT :limit """
            , nativeQuery = true)
    List<UserLogStatsDTO> findTopUsersByLogCount(Integer limit);

    @Query(value = """
        SELECT l.* from log_entry
        WHERE l.deleted_at IS NULL
          AND (:appUserId IS NULL OR l.id = :appUserId)
          AND (:service IS NULL OR l.source_service = :service)
          AND (:level IS NULL OR l.level = :level)
          AND (CAST(:from AS date) IS NULL OR dc.occurred_at >= CAST(:from AS date))
          AND (CAST(:to AS date) IS NULL OR dc.occurred_at <= CAST(:to AS date))
        ORDER BY dc.created_at
    """, nativeQuery = true)
    List<LogEntry> getLogEntriesFiltered(Integer appUserId, String service, String level, LocalDate startDate, LocalDate endDate);

}
