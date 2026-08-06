package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.ForbiddenAccessException;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.practicecases.StreakValidator;
import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class CreateCheckInUseCaseImpl implements ICreateCheckInUseCase {

    private ICheckInRepository checkInRepository;
    private IPracticeRepository practiceRepository;
    private StreakValidator streakValidator;

    @Override
    public CheckIn create(Long practiceId, Long userId) {
        return this.createWithDetails(practiceId, null, false, null, userId);
    }

    @Override
    public CheckIn createWithDetails(Long practiceId, String content, boolean isPublic, Mood mood, Long userId) {
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new PracticeNotFoundByIdException(practiceId));

        if (!practice.getCreator().getId().equals(userId)) {
            throw new ForbiddenAccessException();
        }

        streakValidator.validateStreak(practice);

        LocalDate today = LocalDate.now();
        if (practice.getLastUpdatedStreak() == null || !practice.getLastUpdatedStreak().toLocalDate().equals(today)) {
            practice.setStreak(practice.getStreak() + 1);
            practice.setLastUpdatedStreak(LocalDateTime.now());
            practiceRepository.save(practice);
        }

        return checkInRepository.save(CheckIn.builder()
                .practice(practice)
                .date(today)
                .streakValue(practice.getStreak())
                .content(content)
                .isPublic(isPublic)
                .mood(mood)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
