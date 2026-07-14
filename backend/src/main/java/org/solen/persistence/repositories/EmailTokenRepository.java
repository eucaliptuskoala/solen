package org.solen.persistence.repositories;

import org.solen.business.repos.IEmailTokenRepository;
import org.solen.domain.email.EmailToken;
import org.solen.persistence.converters.EmailTokenConverter;
import org.solen.persistence.entities.EmailTokenEntity;
import org.solen.persistence.jparepos.EmailTokenJpaRepository;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class EmailTokenRepository implements IEmailTokenRepository{

    private final EmailTokenJpaRepository jpaRepository;

    private final EmailTokenConverter converter;

    @Override
    public EmailToken save(EmailToken token) {
        EmailTokenEntity entity = jpaRepository.save(converter.convertToEntity(token));
        return converter.convertToDomain(entity);
    }

    @Override
    public EmailToken findByToken(String token){
        EmailTokenEntity entity = jpaRepository.findByToken(token);
        return entity != null ? converter.convertToDomain(entity) : null;
    }

    @Override
    public Boolean existsByToken(String token) {
        return jpaRepository.existsByToken(token);
    }
}
