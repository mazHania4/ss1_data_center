package ss1.ong.datacenter.auth.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import ss1.ong.datacenter.auth.users.enums.RolesEnum;
import ss1.ong.datacenter.auth.users.enums.StatusUserEnum;
import ss1.ong.datacenter.common.models.entities.Auditor;

import java.util.List;

/**
 * Usuario interno de la aplicación
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUser extends Auditor {

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String phoneNumber;

    @Column(nullable = true, length = 50)
    private String lastname;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolesEnum role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusUserEnum status;

    @Column(nullable = false)
    private Boolean mfaActivated;

}
