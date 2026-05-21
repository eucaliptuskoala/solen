package org.solen.business.habitcases.creationstrategy;

import org.solen.controller.dto.habit.CreateHabitRequest;
import org.solen.domain.habits.Habit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitCreationStrategyServiceTest {

    @Mock
    private IHabitCreationStrategy categoryStrategy;

    @Mock
    private IHabitCreationStrategy customStrategy;

    private HabitCreationStrategyService strategyService;

    Long userId;
    CreateHabitRequest requestWithCategory;
    CreateHabitRequest requestCustom;
    Habit habitCategory;
    Habit habitCustom;

    @BeforeEach
    void setUp() {

        strategyService = new HabitCreationStrategyService(categoryStrategy, customStrategy);

        habitCategory = Habit.builder().name("categoryHabit").build();
        habitCustom = Habit.builder().name("customHabit").build();

        userId = 1L;

        requestWithCategory = CreateHabitRequest.builder()
                .categoryId(1L)
                .description("desc")
                .build();

        requestCustom = CreateHabitRequest.builder()
                .categoryId(null)
                .description("desc")
                .build();
    }

    @Test
    void getStrategy_usesCategoryStrategy() {
        when(categoryStrategy.createHabit(requestWithCategory, userId)).thenReturn(habitCategory);

        Habit result = strategyService.getStrategy(requestWithCategory, userId);

        verify(categoryStrategy, times(1)).createHabit(requestWithCategory, userId);
        verify(customStrategy, never()).createHabit(any(), any());
        assertEquals(habitCategory, result);
    }

    @Test
    void getStrategy_usesCustomStrategy() {
        when(customStrategy.createHabit(requestCustom, userId)).thenReturn(habitCustom);

        Habit result = strategyService.getStrategy(requestCustom, userId);

        verify(customStrategy, times(1)).createHabit(requestCustom, userId);
        verify(categoryStrategy, never()).createHabit(any(), any());
        assertEquals(habitCustom, result);
    }

}