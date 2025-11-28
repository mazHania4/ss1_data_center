package ss1.ong.datacenter.auth.mfaCodes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MfaCodesRepository extends JpaRepository<MfaCodes, Integer> {

    public Optional<MfaCodes> findByCodeAndAppUser_Username(String validationCode, String username);
    public Optional<MfaCodes> findByCodeAndAppUser_Id(String code, Integer id);
}
