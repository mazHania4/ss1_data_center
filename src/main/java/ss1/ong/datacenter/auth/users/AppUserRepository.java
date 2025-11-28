package ss1.ong.datacenter.auth.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);

    public Optional<AppUser> findUserByUsername(String username);

    public Optional<AppUser> findUserByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void softDeleteById(@Param("id") Integer id);

    List<AppUser> findAllByDeletedAtIsNull();

}
