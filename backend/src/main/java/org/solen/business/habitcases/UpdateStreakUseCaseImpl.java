package org.solen.business.habitcases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.HabitNotFoundByIdException;
import org.solen.business.exceptions.StreakAlreadyUpdatedException;
import org.solen.business.repos.IHabitRepository;
import org.solen.domain.habits.Habit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UpdateStreakUseCaseImpl implements IUpdateStreakUseCase {

    private IHabitRepository repository;
    private StreakValidator streakValidator;

    @Override
    @Transactional
    public Habit updateStreak(Long id) {
        Habit habit = repository.findById(id);
        if (habit == null) {
            throw new HabitNotFoundByIdException(id);
        }
        LocalDateTime now = LocalDateTime.now();

        // Reset to 0 if too many days have passed since last update
        streakValidator.validateStreak(habit);

        // Prevent double-tap on the same day
        if(habit.getLastUpdatedStreak() != null){
            LocalDate last = habit.getLastUpdatedStreak().toLocalDate();
            LocalDate today = now.toLocalDate();

            if(last.equals(today)){
                throw new StreakAlreadyUpdatedException();
            }
        }

        habit.setStreak(habit.getStreak() + 1);
        habit.setLastUpdatedStreak(now);

        return repository.save(habit);
    }
}