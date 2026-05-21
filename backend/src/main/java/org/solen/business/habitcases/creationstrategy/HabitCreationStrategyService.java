package org.solen.business.habitcases.creationstrategy;

import org.solen.controller.dto.habit.CreateHabitRequest;
import org.solen.domain.habits.Habit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

// Router that selects the correct IHabitCreationStrategy based on the request.
// Not a factory — strategies are injected by @Qualifier bean name.
//   categoryId present → CategoryHabitCreationStrategy (links to a category)
//   categoryId null    → CustomHabitCreationStrategy     (free-form habit)
@Service
public class HabitCreationStrategyService {

    @Qualifier("categoryCreationStrategy")
    private final IHabitCreationStrategy categoryStrategy;

    @Qualifier("customCreationStrategy")
    private final IHabitCreationStrategy custom;

    public HabitCreationStrategyService(
            @Qualifier("categoryCreationStrategy") IHabitCreationStrategy categoryStrategy,
            @Qualifier("customCreationStrategy") IHabitCreationStrategy custom
    ) {
        this.categoryStrategy = categoryStrategy;
        this.custom = custom;
    }

    public Habit getStrategy (CreateHabitRequest request, Long userId){
        if(request.getCategoryId() == null){
            return custom.createHabit(request, userId);
        }
        else{
            return categoryStrategy.createHabit(request, userId);
        }
    }
}