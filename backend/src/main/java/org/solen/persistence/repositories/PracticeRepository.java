package org.solen.persistence.repositories;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.solen.persistence.converters.PracticeConverter;
import org.solen.persistence.entities.PracticeEntity;
import org.solen.persistence.jparepos.PracticeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class PracticeRepository implements IPracticeRepository {

    private PracticeJpaRepository jpaRepository;
    private PracticeConverter converter;

    @Override
    public Practice save(Practice practice) {
        PracticeEntity entity = jpaRepository.save(converter.convertToEntity(practice));
        return converter.convertToDomain(entity);
    }

    @Override
    public Practice findById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return jpaRepository.findById(id)
                .map(converter::convertToDomain)
                .orElse(null);
    }

    @Override
    public List<Practice> findAll() {
        return jpaRepository.findAll().stream().map(converter::convertToDomain).toList();
    }

    @Override
    public List<Practice> findByName(String name) {
        return jpaRepository.findByName(name).stream().map(converter::convertToDomain).toList();
    }

    @Override
    public List<Practice> findByCreatorId(Long userId) {
        return jpaRepository.findByCreator_Id(userId).stream().map(converter::convertToDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByPracticeIdAndCreatorEmail(Long practiceId, String email) {
        return jpaRepository.existsByIdAndCreatorEmail(practiceId, email);
    }
}