package org.solen.business.checkin;

import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.practicecases.StreakValidator;
import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
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
class CreateCheckInUseCaseImplTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @Mock
    private IPracticeRepository practiceRepository;

    @Mock
    private StreakValidator streakValidator;

    @InjectMocks
    private CreateCheckInUseCaseImpl createUseCase;

    @Test
    void createCheckIn_success() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Drink Water")
                .streak(10)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(7)
                .build();

        when(practiceRepository.findById(1L)).thenReturn(practice);
        when(checkInRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CheckIn created = createUseCase.create(1L);

        assertNotNull(created);
        assertEquals(10, created.getStreakValue());
        assertNull(created.getContent());
        assertFalse(created.isPublic());
        assertNull(created.getMood());

        verify(practiceRepository, times(1)).findById(1L);
        verify(checkInRepository, times(1)).save(any());
    }

    @Test
    void createCheckInWithDetails_success() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Meditation")
                .streak(8)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(5)
                .build();

        when(practiceRepository.findById(1L)).thenReturn(practice);
        when(checkInRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CheckIn created = createUseCase.createWithDetails(1L, "Peaceful session", true, Mood.GOOD);

        assertNotNull(created);
        assertEquals(8, created.getStreakValue());
        assertEquals("Peaceful session", created.getContent());
        assertTrue(created.isPublic());
        assertEquals(Mood.GOOD, created.getMood());

        verify(practiceRepository, times(1)).findById(1L);
        verify(checkInRepository, times(1)).save(any());
    }

    @Test
    void createCheckIn_practiceNotFound() {
        when(practiceRepository.findById(99L)).thenReturn(null);

        assertThrows(PracticeNotFoundByIdException.class, () -> createUseCase.create(99L));
        verify(practiceRepository, times(1)).findById(99L);
        verify(checkInRepository, never()).save(any());
    }
}
