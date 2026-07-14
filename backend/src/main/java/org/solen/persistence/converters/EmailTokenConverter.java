package org.solen.persistence.converters;

import lombok.AllArgsConstructor;
import org.solen.domain.email.EmailToken;
import org.solen.persistence.entities.EmailTokenEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailTokenConverter {

    private UserConverter userConverter;

    public EmailTokenEntity convertToEntity(EmailToken token) {
        return EmailTokenEntity.builder()
                .id(token.getId())
                .user(userConverter.convertToEntity(token.getUser()))
                .token(token.getToken())
                .expiration(token.getExpiration())
                .build();
    }

    public EmailToken convertToDomain(EmailTokenEntity entity) {
        return EmailToken.builder()
                .id(entity.getId())
                .user(userConverter.convertToDomain(entity.getUser()))
                .token(entity.getToken())
                .expiration(entity.getExpiration())
                .build();
    }
}
