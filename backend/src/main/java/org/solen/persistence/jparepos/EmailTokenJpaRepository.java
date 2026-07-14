package org.solen.persistence.jparepos;

import org.solen.persistence.entities.EmailTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailTokenJpaRepository extends JpaRepository<EmailTokenEntity, Long>{
    EmailTokenEntity findByToken(String token);
    Boolean existsByToken(String token);
}
