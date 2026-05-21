package org.solen.business.checkin;

import org.solen.domain.checkin.CheckIn;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IGetCheckInsForUserUseCase {
    List<CheckIn> getCheckInsForUser(Long userId, LocalDate from, LocalDate to);
    Set<Long> findPracticeIdsCheckedInTodayByUserId(Long userId);
}
