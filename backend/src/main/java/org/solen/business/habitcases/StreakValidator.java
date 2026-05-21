package org.solen.business.habitcases;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IHabitRepository;
import org.solen.domain.habits.Habit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

// Checks whether a habit's streak has expired due to inactivity.
// If daysSinceLastUpdate > thresholdDays, the streak resets to 0.
// Called before every streak increment and on habit fetch so the frontend
// always sees a fresh value without needing an explicit "miss day" action.
@Service
@AllArgsConstructor
public class StreakValidator {

    private IHabitRepository repository;

    @Transactional
    public void validateStreak(Habit habit) {
        // Never updated → still in initial state, no reset needed
        if (habit.getLastUpdatedStreak() == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        long daysSinceLast = Duration.between(habit.getLastUpdatedStreak(), now).toDays();

        if (daysSinceLast > habit.getThresholdDays()) {
            habit.setStreak(0);
            repository.save(habit);
        }
    }
}