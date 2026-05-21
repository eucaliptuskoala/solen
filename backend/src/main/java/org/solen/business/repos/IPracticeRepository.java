package org.solen.business.repos;

import org.solen.domain.practices.Practice;

import java.util.List;

public interface IPracticeRepository {
    Practice save(Practice practice);
    Practice findById(Long id);
    List<Practice> findAll();
    List<Practice> findByName(String name);
    List<Practice> findByCreatorId(Long userId);
    void deleteById(Long id);
    boolean existsByPracticeIdAndCreatorEmail(Long practiceId, String email);
}