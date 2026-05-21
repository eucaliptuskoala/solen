package org.solen.controller.mappers;

import org.solen.controller.dto.checkin.CheckInDto;
import org.solen.controller.dto.habit.HabitDto;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.habits.Habit;
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
class CheckInMapperTest {

    @Mock
    private HabitMapper habitMapper;

    @InjectMocks
    private CheckInMapper mapper;

    @Test
    void convertToDto_null_returnsNull() {
        assertNull(mapper.convertToDto(null));
    }

    @Test
    void convertToDto_mapsAllFields() {
        Habit habit = Habit.builder().id(1L).build();
        HabitDto habitDto = HabitDto.builder().id(1L).build();
        when(habitMapper.convertToDto(habit)).thenReturn(habitDto);

        CheckIn checkIn = CheckIn.builder()
                .id(1L)
                .habit(habit)
                .date(LocalDate.of(2026, 5, 21))
                .streakValue(3)
                .content("Good day")
                .isPublic(true)
                .mood(Mood.GOOD)
                .createdAt(LocalDateTime.of(2026, 5, 21, 10, 0))
                .build();

        CheckInDto dto = mapper.convertToDto(checkIn);

        assertEquals(1L, dto.getId());
        assertEquals(LocalDate.of(2026, 5, 21), dto.getDate());
        assertEquals(3, dto.getStreakValue());
        assertEquals("Good day", dto.getContent());
        assertTrue(dto.isPublic());
        assertEquals(Mood.GOOD, dto.getMood());
        assertNotNull(dto.getCreatedAt());
        assertEquals(1L, dto.getHabit().getId());
        verify(habitMapper).convertToDto(habit);
    }
}
