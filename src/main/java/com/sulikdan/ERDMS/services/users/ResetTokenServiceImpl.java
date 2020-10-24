package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.entities.users.ResetToken;
import com.sulikdan.ERDMS.repositories.ResetTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 19-Oct-20
 * @see ResetTokenService
 */
@Slf4j
@AllArgsConstructor
@Service
public class ResetTokenServiceImpl implements ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    @Override
    public void saveResetToken(ResetToken resetToken) {
        resetTokenRepository.save(resetToken);
    }

    @Override
    public void deleteResetToken(String id) {
        resetTokenRepository.deleteById(id);
    }

    @Override
    public Optional<ResetToken> findTokenById(String id) {
        return resetTokenRepository.findById(id);
    }
}
