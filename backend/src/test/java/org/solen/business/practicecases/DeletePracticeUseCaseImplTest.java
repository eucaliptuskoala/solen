package org.solen.business.practicecases;

import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePracticeUseCaseImplTest {

    @Mock
    IPracticeRepository repository;

    @InjectMocks
    DeletePracticeUseCaseImpl deletePracticeUseCaseImpl;

    @Test
    void deletePractice_success() {
        when(repository.findById(1L)).thenReturn(Practice.builder().build());
        deletePracticeUseCaseImpl.deletePractice(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deletePractice_notFound_throws() {
        when(repository.findById(1L)).thenReturn(null);
        assertThrows(PracticeNotFoundByIdException.class, () -> deletePracticeUseCaseImpl.deletePractice(1L));
        verify(repository, never()).deleteById(any());
    }
}