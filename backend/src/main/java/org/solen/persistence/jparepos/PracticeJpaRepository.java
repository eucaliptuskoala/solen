package org.solen.persistence.jparepos;

import org.solen.persistence.entities.PracticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeJpaRepository extends JpaRepository<PracticeEntity, Long> {
    List<PracticeEntity> findByName(String name);
    List<PracticeEntity> findByCreator_Id(Long userId);
    boolean existsByIdAndCreatorEmail(Long id, String email);
}
