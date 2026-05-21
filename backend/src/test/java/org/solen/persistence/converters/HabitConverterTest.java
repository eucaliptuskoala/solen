package org.solen.persistence.converters;

import org.solen.domain.habits.Category;
import org.solen.domain.habits.Habit;
import org.solen.domain.users.User;
import org.solen.persistence.entities.CategoryEntity;
import org.solen.persistence.entities.HabitEntity;
import org.solen.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitConverterTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private CategoryConverter categoryConverter;

    @InjectMocks
    private HabitConverter converter;

    @Test
    void convertToEntity_mapsAllFields() {
        User user = User.builder().id(1L).build();
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        Category category = Category.builder().id(1L).name("Fitness").build();
        CategoryEntity categoryEntity = CategoryEntity.builder().id(1L).name("Fitness").build();
        when(userConverter.convertToEntity(user)).thenReturn(userEntity);
        when(categoryConverter.convertToEntity(category)).thenReturn(categoryEntity);

        Habit habit = Habit.builder()
                .id(1L).name("Run").description("Run daily")
                .streak(5).lastUpdatedStreak(LocalDateTime.of(2026, 5, 21, 10, 0))
                .thresholdDays(2).category(category).creator(user)
                .build();

        HabitEntity entity = converter.convertToEntity(habit);

        assertEquals(1L, entity.getId());
        assertEquals("Run", entity.getName());
        assertEquals(5, entity.getStreak());
        assertEquals(2, entity.getThresholdDays());
        assertNotNull(entity.getCreator());
        assertNotNull(entity.getCategory());
        verify(userConverter).convertToEntity(user);
        verify(categoryConverter).convertToEntity(category);
    }

    @Test
    void convertToDomain_mapsAllFields() {
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        CategoryEntity categoryEntity = CategoryEntity.builder().id(1L).name("Fitness").build();
        Category category = Category.builder().id(1L).name("Fitness").build();
        when(userConverter.convertToDomain(userEntity)).thenReturn(user);
        when(categoryConverter.convertToDomain(categoryEntity)).thenReturn(category);

        HabitEntity entity = HabitEntity.builder()
                .id(1L).name("Run").description("Run daily")
                .streak(5).lastUpdatedStreak(LocalDateTime.of(2026, 5, 21, 10, 0))
                .thresholdDays(2).category(categoryEntity).creator(userEntity)
                .build();

        Habit habit = converter.convertToDomain(entity);

        assertEquals(1L, habit.getId());
        assertEquals("Run", habit.getName());
        assertEquals(5, habit.getStreak());
        assertEquals(2, habit.getThresholdDays());
        assertNotNull(habit.getCreator());
        assertNotNull(habit.getCategory());
        verify(userConverter).convertToDomain(userEntity);
        verify(categoryConverter).convertToDomain(categoryEntity);
    }

    @Test
    void convertToEntity_nullCategory() {
        User user = User.builder().id(1L).build();
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        when(userConverter.convertToEntity(user)).thenReturn(userEntity);
        when(categoryConverter.convertToEntity(null)).thenReturn(null);

        Habit habit = Habit.builder()
                .id(1L).name("Read").streak(0).thresholdDays(1)
                .category(null).creator(user)
                .build();

        HabitEntity entity = converter.convertToEntity(habit);

        assertNull(entity.getCategory());
    }
}
