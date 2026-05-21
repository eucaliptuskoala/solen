package org.solen.business.practicecases;

import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreakValidatorTest {

    @Mock
    private IPracticeRepository repository;

    @InjectMocks
    private StreakValidator streakValidator;

    @Test
    void validateStreak_dropStreak() {
        User user =  User.builder()
                .id(1L)
                .name("Test1")
                .email("test@mail.com")
                .password("12345")
                .isAdmin(false)
                .build();

        Practice practice = Practice.builder()
                .id(1L)
                .name("Test1")
                .description("Test1")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.of(2025, 11, 2, 0, 0))
                .thresholdDays(1)
                .creator(user)
                .build();

        streakValidator.validateStreak(practice);

        assertEquals(0, practice.getStreak());
        verify(repository, times(1)).save(practice);
    }

    @Test
    void validateStreak_doesNotDropStreak() {
        Practice practice = Practice.builder()
                .streak(5)
                .lastUpdatedStreak(LocalDateTime.now().minusDays(1))
                .thresholdDays(2)
                .build();

        streakValidator.validateStreak(practice);

        assertEquals(5, practice.getStreak());
        verify(repository, never()).save(any(Practice.class));
    }

    @Test
    void validateStreak_noLastUpdate_doesNothing() {
        Practice practice = Practice.builder()
                .streak(5)
                .lastUpdatedStreak(null)
                .thresholdDays(1)
                .build();

        streakValidator.validateStreak(practice);

        assertEquals(5, practice.getStreak());
        verify(repository, never()).save(any(Practice.class));
    }
}