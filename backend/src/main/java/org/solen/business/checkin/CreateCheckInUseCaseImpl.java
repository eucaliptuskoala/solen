package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.practicecases.StreakValidator;
import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CreateCheckInUseCaseImpl implements ICreateCheckInUseCase {

    private ICheckInRepository checkInRepository;
    private IPracticeRepository practiceRepository;
    private StreakValidator streakValidator;

    @Override
    public CheckIn create(Long practiceId) {
        return this.createWithDetails(practiceId, null, false, null);
    }

    @Override
    public CheckIn createWithDetails(Long practiceId, String content, boolean isPublic, Mood mood) {
        Practice practice = practiceRepository.findById(practiceId);
        if (practice == null) {
            throw new PracticeNotFoundByIdException(practiceId);
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
