package org.solen.business.repos;

import org.solen.domain.checkin.CheckIn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ICheckInRepository {
    CheckIn save(CheckIn checkIn);
    Optional<CheckIn> findById(Long id);
    List<CheckIn> findByPracticeCreatorId(Long userId);
    List<CheckIn> findCheckInsForUser(Long userId, LocalDate from, LocalDate to);
    List<CheckIn> findPublicCheckIns();
    List<CheckIn> findPublicCheckInsForCategories(List<Long> categoryIds, Long userId);
    boolean findByCheckInIdAndEmail(Long checkInId, String email);
    Set<Long> findPracticeIdsCheckedInTodayByUserId(Long userId);
    void deleteById(Long id);
}
