package ss1.ong.datacenter.auth.mfaCodes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ss1.ong.datacenter.auth.users.AppUser;
import ss1.ong.datacenter.common.exceptions.NotFoundException;
import ss1.ong.datacenter.common.utils.MailService;

import java.time.LocalDateTime;
import java.util.Random;

/**
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MfaCodesService {
    private final MailService mailService;
    private final MfaCodesRepository mfaCodesRepository;

    public void saveVerification(String verificationCode, AppUser appUser){
        MfaCodes verificate = new MfaCodes(verificationCode, appUser);
        mfaCodesRepository.save(verificate);
    }

    public void sendVerificationEmail(AppUser appUser, String verificationCode){
        mailService.sendVerifyEmail(
                appUser.getEmail(),
                appUser.getName() + appUser.getLastname(),
                appUser.getId(),
                verificationCode
        );
    }

    public MfaCodes verificateCode(String code, String username) throws NotFoundException {
        MfaCodes mfaCodes = mfaCodesRepository.findByCodeAndAppUser_Username(
                code, username
        ).orElseThrow(() -> new NotFoundException("No se encontro el codigo"));
        if(mfaCodes.getExpirationTime().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("El codigo ya vencio");
        }
        mfaCodesRepository.delete(mfaCodes);
        return mfaCodes;
    }

    public String getVerificationCode(){
        Random random = new Random();
        return String.format("%06d", random.nextInt(1_000_000));
    }

}
