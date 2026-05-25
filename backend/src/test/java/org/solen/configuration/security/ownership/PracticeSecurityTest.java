package org.solen.configuration.security.ownership;

import org.solen.business.repos.IPracticeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticeSecurityTest {

    @Mock
    private IPracticeRepository practiceRepository;

    @InjectMocks
    private PracticeSecurity practiceSecurity;

    @Test
    void isOwnerByEmail_returnsTrue() {
        when(practiceRepository.existsByPracticeIdAndCreatorEmail(1L, "owner@test.com")).thenReturn(true);

        assertTrue(practiceSecurity.isOwnerByEmail(1L, "owner@test.com"));
    }

    @Test
    void isOwnerByEmail_returnsFalse() {
        when(practiceRepository.existsByPracticeIdAndCreatorEmail(99L, "other@test.com")).thenReturn(false);

        assertFalse(practiceSecurity.isOwnerByEmail(99L, "other@test.com"));
    }
}
