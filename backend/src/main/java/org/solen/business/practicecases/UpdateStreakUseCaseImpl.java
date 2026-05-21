package org.solen.business.practicecases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.exceptions.StreakAlreadyUpdatedException;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UpdateStreakUseCaseImpl implements IUpdateStreakUseCase {

    private IPracticeRepository repository;
    private StreakValidator streakValidator;

    @Override
    @Transactional
    public Practice updateStreak(Long id) {
        Practice practice = repository.findById(id);
        if (practice == null) {
            throw new PracticeNotFoundByIdException(id);
        }
        LocalDateTime now = LocalDateTime.now();

        // Reset to 0 if too many days have passed since last update
        streakValidator.validateStreak(practice);

        // Prevent double-tap on the same day
        if(practice.getLastUpdatedStreak() != null){
            LocalDate last = practice.getLastUpdatedStreak().toLocalDate();
            LocalDate today = now.toLocalDate();

            if(last.equals(today)){
                throw new StreakAlreadyUpdatedException();
            }
        }

        practice.setStreak(practice.getStreak() + 1);
        practice.setLastUpdatedStreak(now);

        return repository.save(practice);
    }
}