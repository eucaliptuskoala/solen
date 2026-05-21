package org.solen.business.habitcases;

import org.solen.business.exceptions.HabitNotFoundByIdException;
import org.solen.business.repos.IHabitRepository;
import org.solen.domain.habits.Habit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteHabitUseCaseImplTest {

    @Mock
    IHabitRepository repository;

    @InjectMocks
    DeleteHabitUseCaseImpl deleteHabitUseCaseImpl;

    @Test
    void deleteHabit_success() {
        when(repository.findById(1L)).thenReturn(Habit.builder().build());
        deleteHabitUseCaseImpl.deleteHabit(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteHabit_notFound_throws() {
        when(repository.findById(1L)).thenReturn(null);
        assertThrows(HabitNotFoundByIdException.class, () -> deleteHabitUseCaseImpl.deleteHabit(1L));
        verify(repository, never()).deleteById(any());
    }
}