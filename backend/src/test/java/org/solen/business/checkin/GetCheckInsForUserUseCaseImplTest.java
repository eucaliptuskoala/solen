package org.solen.business.checkin;

import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.habits.Habit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCheckInsForUserUseCaseImplTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @Mock
    private CheckInTimelineBuilder timelineBuilder;

    @InjectMocks
    private GetCheckInsForUserUseCaseImpl getCheckInsUseCase;

    private CheckIn checkIn;

    @BeforeEach
    void setUp() {
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .build();

        checkIn = CheckIn.builder()
                .id(1L)
                .habit(habit)
                .date(LocalDate.now())
                .streakValue(1)
                .content("Test content")
                .isPublic(true)
                .mood(Mood.GOOD)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getCheckInsForUser_withDateRange() {
        when(checkInRepository.findCheckInsForUser(1L, LocalDate.now(), LocalDate.now()))
                .thenReturn(List.of(checkIn));
        when(timelineBuilder.buildTimeline(any())).thenReturn(List.of(checkIn));

        List<CheckIn> result = getCheckInsUseCase.getCheckInsForUser(1L, LocalDate.now(), LocalDate.now());

        assertEquals(1, result.size());
        assertEquals("Test content", result.get(0).getContent());
        assertEquals(Mood.GOOD, result.get(0).getMood());
        verify(checkInRepository, times(1)).findCheckInsForUser(1L, LocalDate.now(), LocalDate.now());
    }

    @Test
    void getCheckInsForUser_withoutDateRange() {
        when(checkInRepository.findByHabitCreatorId(1L)).thenReturn(List.of(checkIn));
        when(timelineBuilder.buildTimeline(any())).thenReturn(List.of(checkIn));

        List<CheckIn> result = getCheckInsUseCase.getCheckInsForUser(1L, null, null);

        assertEquals(1, result.size());
        verify(checkInRepository, times(1)).findByHabitCreatorId(1L);
    }

    @Test
    void getCheckInsForUser_empty() {
        when(checkInRepository.findByHabitCreatorId(1L)).thenReturn(List.of());
        when(timelineBuilder.buildTimeline(any())).thenReturn(List.of());

        List<CheckIn> result = getCheckInsUseCase.getCheckInsForUser(1L, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void getCheckInsForUser_withFromOnly() {
        LocalDate from = LocalDate.of(2026, 5, 1);
        when(checkInRepository.findCheckInsForUser(1L, from, LocalDate.now()))
                .thenReturn(List.of(checkIn));
        when(timelineBuilder.buildTimeline(any())).thenReturn(List.of(checkIn));

        List<CheckIn> result = getCheckInsUseCase.getCheckInsForUser(1L, from, null);

        assertEquals(1, result.size());
        verify(checkInRepository).findCheckInsForUser(1L, from, LocalDate.now());
    }

    @Test
    void getCheckInsForUser_withToOnly() {
        LocalDate to = LocalDate.of(2026, 5, 21);
        CheckIn before = CheckIn.builder()
                .id(1L).habit(checkIn.getHabit()).date(LocalDate.of(2026, 5, 20))
                .streakValue(1).content("before").mood(Mood.OKAY)
                .build();
        CheckIn after = CheckIn.builder()
                .id(2L).habit(checkIn.getHabit()).date(LocalDate.of(2026, 5, 25))
                .streakValue(1).content("after").mood(Mood.OKAY)
                .build();
        when(checkInRepository.findByHabitCreatorId(1L)).thenReturn(List.of(before, after));
        when(timelineBuilder.buildTimeline(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<CheckIn> result = getCheckInsUseCase.getCheckInsForUser(1L, null, to);

        assertEquals(1, result.size());
        assertEquals("before", result.get(0).getContent());
        verify(checkInRepository).findByHabitCreatorId(1L);
    }
}
