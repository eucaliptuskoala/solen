package org.solen.business.repos;

import org.solen.domain.email.EmailToken;

public interface IEmailTokenRepository {
    EmailToken save(EmailToken token);
    EmailToken findByToken(String token);
    Boolean existsByToken(String token);
}