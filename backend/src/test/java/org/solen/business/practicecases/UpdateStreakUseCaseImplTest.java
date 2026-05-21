package org.solen.business.practicecases;

import org.solen.business.exceptions.StreakAlreadyUpdatedException;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStreakUseCaseImplTest {

    @Mock
    private IPracticeRepository repository;

    @Mock
    private StreakValidator streakValidator;

    @InjectMocks
    private UpdateStreakUseCaseImpl updateStreakUseCaseImpl;

    @Test
    void updateStreakUseCase_success() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Test1")
                .description("Test1")
                .streak(1)
                .lastUpdatedStreak(null)
                .thresholdDays(1)
                .creator(null)
                .build();

        when(repository.findById(1L)).thenReturn(practice);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Practice updated = updateStreakUseCaseImpl.updateStreak(1L);

        assertEquals(2, updated.getStreak());
        assertNotNull(updated.getLastUpdatedStreak());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(practice);
    }

    @Test
    void updateStreakUseCase_streakAlreadyUpdated() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Test1")
                .description("Test1")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(null)
                .build();

        when(repository.findById(1L)).thenReturn(practice);

        StreakAlreadyUpdatedException exception =
                assertThrows(StreakAlreadyUpdatedException.class, () -> updateStreakUseCaseImpl.updateStreak(1L));

        assertEquals("Streak already updated!", exception.getMessage());
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any());
    }
}
