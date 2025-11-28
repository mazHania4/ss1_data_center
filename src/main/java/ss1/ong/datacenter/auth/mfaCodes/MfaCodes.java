package ss1.ong.datacenter.auth.mfaCodes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import ss1.ong.datacenter.auth.users.AppUser;
import ss1.ong.datacenter.common.models.entities.Auditor;

import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class MfaCodes extends Auditor {

    @Column(nullable = false, unique = false, length = 6)
    private String code;

    @Column(nullable = false, unique = false)
    private LocalDateTime expirationTime;

    @Column(nullable = false, unique = false)
    private Boolean isUsed = false;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser appUser;

    public MfaCodes(String code, AppUser appUser){
        this.code = code;
        this.appUser =appUser;
        this.expirationTime = LocalDateTime.now().plusMinutes(15);
    }

}
