package org.solen.business.checkin;

import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCheckInUseCaseImplTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @InjectMocks
    private UpdateCheckInUseCaseImpl updateUseCase;

    @Test
    void update_existingCheckIn_updatesAndSaves() {
        CheckIn existing = CheckIn.builder().id(1L).content("old").isPublic(false).mood(Mood.OKAY).build();
        when(checkInRepository.findById(1L)).thenReturn(existing);
        when(checkInRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CheckIn result = updateUseCase.update(1L, "new content", true, Mood.GOOD);

        assertEquals("new content", result.getContent());
        assertTrue(result.isPublic());
        assertEquals(Mood.GOOD, result.getMood());
        verify(checkInRepository).findById(1L);
        verify(checkInRepository).save(existing);
    }

    @Test
    void update_nonExistingCheckIn_throws() {
        when(checkInRepository.findById(99L)).thenReturn(null);

        assertThrows(CheckInNotFoundException.class,
                () -> updateUseCase.update(99L, "x", false, Mood.OKAY));
        verify(checkInRepository).findById(99L);
        verify(checkInRepository, never()).save(any());
    }
}
