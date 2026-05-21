package org.solen.business.checkin.fypstrategy;

import org.solen.business.repos.IHabitRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.habits.Habit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private IRecommendationStrategy habitNameBased;

    @Mock
    private IRecommendationStrategy defaultStrategy;

    @Mock
    private IHabitRepository habitRepository;

    private RecommendationService service;

    @Test
    void findPublicCheckIns_userHasHabits_usesHabitBased() {
        service = new RecommendationService(habitNameBased, defaultStrategy, habitRepository);

        when(habitRepository.findByCreatorId(1L)).thenReturn(List.of(Habit.builder().id(1L).build()));
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(habitNameBased.findPublicCheckIns(1L)).thenReturn(List.of(ci));

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(habitNameBased).findPublicCheckIns(1L);
        verify(defaultStrategy, never()).findPublicCheckIns(anyLong());
    }

    @Test
    void findPublicCheckIns_userHasNoHabits_usesDefault() {
        service = new RecommendationService(habitNameBased, defaultStrategy, habitRepository);

        when(habitRepository.findByCreatorId(1L)).thenReturn(List.of());
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(defaultStrategy.findPublicCheckIns(1L)).thenReturn(List.of(ci));

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(defaultStrategy).findPublicCheckIns(1L);
        verify(habitNameBased, never()).findPublicCheckIns(anyLong());
    }
}
