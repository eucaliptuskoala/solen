package org.solen.configuration.security.ownership;

import org.solen.business.repos.ICheckInRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInSecurityTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @InjectMocks
    private CheckInSecurity checkInSecurity;

    @Test
    void isOwnerByEmail_returnsTrue() {
        when(checkInRepository.findByCheckInIdAndEmail(1L, "owner@test.com")).thenReturn(true);

        assertTrue(checkInSecurity.isOwnerByEmail(1L, "owner@test.com"));
    }

    @Test
    void isOwnerByEmail_returnsFalse() {
        when(checkInRepository.findByCheckInIdAndEmail(99L, "other@test.com")).thenReturn(false);

        assertFalse(checkInSecurity.isOwnerByEmail(99L, "other@test.com"));
    }
}
