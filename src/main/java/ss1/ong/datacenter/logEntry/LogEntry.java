package ss1.ong.datacenter.logEntry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import ss1.ong.datacenter.auth.users.AppUser;
import ss1.ong.datacenter.common.models.entities.Auditor;
import ss1.ong.datacenter.logEntry.enums.LogLevelEnum;
import ss1.ong.datacenter.logEntry.enums.ServiceForLogEnum;

import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LogEntry extends Auditor {

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ServiceForLogEnum sourceService;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LogLevelEnum level;

    @Column(nullable = false)
    private String eventName;

    @ManyToOne
    @JoinColumn
    private AppUser appUser;

    @Column
    private String relatedEntity;

    @Column
    private Integer relatedEntityId;

    @Column(columnDefinition = "TEXT")
    private String details;

}
