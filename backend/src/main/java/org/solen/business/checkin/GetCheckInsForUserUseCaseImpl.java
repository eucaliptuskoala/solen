package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GetCheckInsForUserUseCaseImpl implements IGetCheckInsForUserUseCase {

    private ICheckInRepository checkInRepository;
    private CheckInTimelineBuilder timelineBuilder;

    @Override
    @Transactional(readOnly = true)
    public List<CheckIn> getCheckInsForUser(Long userId, LocalDate from, LocalDate to) {
        List<CheckIn> raw;
        if (from != null && to != null) {
            raw = checkInRepository.findCheckInsForUser(userId, from, to);
        } else if (from != null) {
            raw = checkInRepository.findCheckInsForUser(userId, from, LocalDate.now());
        } else if (to != null) {
            raw = checkInRepository.findByPracticeCreatorId(userId).stream()
                    .filter(ci -> !ci.getDate().isAfter(to))
                    .toList();
        } else {
            raw = checkInRepository.findByPracticeCreatorId(userId);
        }
        return timelineBuilder.buildTimeline(raw);
    }

    @Override
    public Set<Long> findPracticeIdsCheckedInTodayByUserId(Long userId) {
        return checkInRepository.findPracticeIdsCheckedInTodayByUserId(userId);
    }
}
