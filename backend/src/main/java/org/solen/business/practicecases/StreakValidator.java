package org.solen.business.practicecases;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

// Checks whether a practice's streak has expired due to inactivity.
// If daysSinceLastUpdate > thresholdDays, the streak resets to 0.
// Called before every streak increment and on practice fetch so the frontend
// always sees a fresh value without needing an explicit "miss day" action.
@Service
@AllArgsConstructor
public class StreakValidator {

    private IPracticeRepository repository;

    @Transactional
    public void validateStreak(Practice practice) {
        // Never updated → still in initial state, no reset needed
        if (practice.getLastUpdatedStreak() == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        long daysSinceLast = Duration.between(practice.getLastUpdatedStreak(), now).toDays();

        if (daysSinceLast > practice.getThresholdDays()) {
            practice.setStreak(0);
            repository.save(practice);
        }
    }
}