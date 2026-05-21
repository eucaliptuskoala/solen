package org.solen.business.checkin;

import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCheckInUseCaseImplTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @InjectMocks
    private DeleteCheckInUseCaseImpl deleteUseCase;

    @Test
    void delete_existingCheckIn_deletes() {
        when(checkInRepository.findById(1L)).thenReturn(CheckIn.builder().id(1L).build());

        deleteUseCase.delete(1L);

        verify(checkInRepository).findById(1L);
        verify(checkInRepository).deleteById(1L);
    }

    @Test
    void delete_nonExistingCheckIn_throws() {
        when(checkInRepository.findById(99L)).thenReturn(null);

        assertThrows(CheckInNotFoundException.class, () -> deleteUseCase.delete(99L));
        verify(checkInRepository).findById(99L);
        verify(checkInRepository, never()).deleteById(anyLong());
    }
}
