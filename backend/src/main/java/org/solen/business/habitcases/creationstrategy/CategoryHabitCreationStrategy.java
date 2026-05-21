package org.solen.business.habitcases.creationstrategy;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.exceptions.HabitAlreadyExistsException;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.business.repos.IHabitRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.controller.dto.habit.CreateHabitRequest;
import org.solen.domain.habits.Category;
import org.solen.domain.habits.Habit;
import org.solen.domain.users.User;

import static org.solen.business.habitcases.creationstrategy.NameUtils.normalizeName;
import org.springframework.stereotype.Service;

import java.util.List;

// Strategy: creates a habit linked to an existing category.
// Used when the client provides a categoryId in the create request.
@Service("categoryCreationStrategy")
@AllArgsConstructor
public class CategoryHabitCreationStrategy implements IHabitCreationStrategy {

    private ICategoryRepository categoryRepository;
    private IHabitRepository habitRepository;
    private IUserRepository userRepository;

    @Override
    public Habit createHabit(CreateHabitRequest request, Long userId) {
        // Look up the category the user wants to tag this habit with
        Category category = categoryRepository.findById(request.getCategoryId());

        if(category == null) {
            throw new CategoryNotFoundByIdException(request.getCategoryId());
        }

        User user = userRepository.findById(userId);
        if(user == null) {
            throw new UserNotFoundByIdException(userId);
        }

        // Prevent duplicate habit names for the same user
        List<Habit> existingHabits = habitRepository.findByCreatorId(user.getId());
        String newHabitName = normalizeName(request.getName());

        if(existingHabits.stream().anyMatch(habit -> habit.getName().equals(newHabitName))) {
            throw new HabitAlreadyExistsException();
        }

        return habitRepository.save(Habit.builder()
                .name(newHabitName)
                .description(request.getDescription())
                .streak(0)
                .thresholdDays(1)
                .creator(user)
                .category(category)
                .build());
    }
}
