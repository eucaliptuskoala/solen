package org.solen.persistence.converters;

import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.habits.Habit;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.entities.HabitEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInConverterTest {

    @Mock
    private HabitConverter habitConverter;

    @InjectMocks
    private CheckInConverter converter;

    @Test
    void convertToEntity_mapsAllFields() {
        Habit habit = Habit.builder().id(1L).build();
        HabitEntity habitEntity = HabitEntity.builder().id(1L).build();
        when(habitConverter.convertToEntity(habit)).thenReturn(habitEntity);

        CheckIn checkIn = CheckIn.builder()
                .id(1L).habit(habit)
                .date(LocalDate.of(2026, 5, 21))
                .streakValue(3).content("Good").isPublic(true)
                .mood(Mood.GOOD).createdAt(LocalDateTime.of(2026, 5, 21, 10, 0))
                .build();

        CheckInEntity entity = converter.convertToEntity(checkIn);

        assertEquals(1L, entity.getId());
        assertEquals(LocalDate.of(2026, 5, 21), entity.getDate());
        assertEquals(3, entity.getStreakValue());
        assertTrue(entity.isPublic());
        assertEquals(Mood.GOOD, entity.getMood());
        assertNotNull(entity.getHabit());
        verify(habitConverter).convertToEntity(habit);
    }

    @Test
    void convertToDomain_mapsAllFields() {
        HabitEntity habitEntity = HabitEntity.builder().id(1L).build();
        Habit habit = Habit.builder().id(1L).build();
        when(habitConverter.convertToDomain(habitEntity)).thenReturn(habit);

        CheckInEntity entity = CheckInEntity.builder()
                .id(1L).habit(habitEntity)
                .date(LocalDate.of(2026, 5, 21))
                .streakValue(3).content("Good").isPublic(true)
                .mood(Mood.GOOD).createdAt(LocalDateTime.of(2026, 5, 21, 10, 0))
                .build();

        CheckIn checkIn = converter.convertToDomain(entity);

        assertEquals(1L, checkIn.getId());
        assertEquals(LocalDate.of(2026, 5, 21), checkIn.getDate());
        assertEquals(3, checkIn.getStreakValue());
        assertEquals("Good", checkIn.getContent());
        assertTrue(checkIn.isPublic());
        assertEquals(Mood.GOOD, checkIn.getMood());
        assertNotNull(checkIn.getHabit());
        verify(habitConverter).convertToDomain(habitEntity);
    }
}
