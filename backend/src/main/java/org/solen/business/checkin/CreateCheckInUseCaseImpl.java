package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.HabitNotFoundByIdException;
import org.solen.business.habitcases.StreakValidator;
import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IHabitRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.habits.Habit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CreateCheckInUseCaseImpl implements ICreateCheckInUseCase {

    private ICheckInRepository checkInRepository;
    private IHabitRepository habitRepository;
    private StreakValidator streakValidator;

    @Override
    public CheckIn create(Long habitId) {
        return createWithDetails(habitId, null, false, null);
    }

    @Override
    @Transactional
    public CheckIn createWithDetails(Long habitId, String content, boolean isPublic, Mood mood) {
        Habit habit = habitRepository.findById(habitId);
        if (habit == null) {
            throw new HabitNotFoundByIdException(habitId);
        }

        streakValidator.validateStreak(habit);

        LocalDate today = LocalDate.now();
        if (habit.getLastUpdatedStreak() == null || !habit.getLastUpdatedStreak().toLocalDate().equals(today)) {
            habit.setStreak(habit.getStreak() + 1);
            habit.setLastUpdatedStreak(LocalDateTime.now());
            habitRepository.save(habit);
        }

        return checkInRepository.save(CheckIn.builder()
                .habit(habit)
                .date(today)
                .streakValue(habit.getStreak())
                .content(content)
                .isPublic(isPublic)
                .mood(mood)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
