package com.ficklebytes.service;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.PasswordResetToken;
import com.ficklebytes.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public void savePasswordResetToken(AcademyUser user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUserId(user.getUserId());
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(resetToken);
    }

    public Optional<PasswordResetToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void invalidateToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}
