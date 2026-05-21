package org.solen.controller.mappers;

import org.solen.controller.dto.habit.HabitDto;
import org.solen.domain.habits.Category;
import org.solen.domain.habits.Habit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HabitMapperTest {

    @InjectMocks
    private HabitMapper mapper;

    @Test
    void convertToDto_mapsAllFields() {
        Habit habit = Habit.builder()
                .id(1L)
                .name("Read")
                .description("Read daily")
                .streak(5)
                .lastUpdatedStreak(LocalDateTime.of(2026, 5, 21, 10, 0))
                .thresholdDays(2)
                .category(Category.builder().id(1L).name("Reading").build())
                .build();

        HabitDto dto = mapper.convertToDto(habit);

        assertEquals(1L, dto.getId());
        assertEquals("Read", dto.getName());
        assertEquals("Read daily", dto.getDescription());
        assertEquals(5, dto.getStreak());
        assertEquals(1L, dto.getCategoryId());
        assertEquals("Reading", dto.getCategoryName());
        assertFalse(dto.isCheckedInToday());
    }

    @Test
    void convertToDto_nullCategory_setsNullCategoryFields() {
        Habit habit = Habit.builder()
                .id(1L).name("Test").streak(0).thresholdDays(1)
                .category(null)
                .build();

        HabitDto dto = mapper.convertToDto(habit);

        assertNull(dto.getCategoryId());
        assertNull(dto.getCategoryName());
    }

    @Test
    void convertToDto_withCheckedInTodayIds_marksCheckedIn() {
        Habit habit = Habit.builder()
                .id(1L).name("Test").streak(0).thresholdDays(1)
                .build();

        HabitDto dto = mapper.convertToDto(habit, Set.of(1L));

        assertTrue(dto.isCheckedInToday());
    }

    @Test
    void convertToDto_withCheckedInTodayIds_notCheckedIn() {
        Habit habit = Habit.builder()
                .id(1L).name("Test").streak(0).thresholdDays(1)
                .build();

        HabitDto dto = mapper.convertToDto(habit, Set.of(2L));

        assertFalse(dto.isCheckedInToday());
    }
}
