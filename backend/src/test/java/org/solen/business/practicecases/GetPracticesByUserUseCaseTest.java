package org.solen.business.practicecases;

import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPracticesByUserUseCaseTest {

    @Mock
    private IPracticeRepository repository;

    @Mock
    private StreakValidator streakValidator;

    @InjectMocks
    private GetPracticesByUserUseCaseImpl getPracticesByUserUseCase;

    List<Practice> practices;

    @BeforeEach
    void setUp() {

        practices = new ArrayList<>();

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
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        practices.add(practice);

        Practice practice2 = Practice.builder()
                .id(1L)
                .name("Test1")
                .description("Test1")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        practices.add(practice2);

        Practice practice3 = Practice.builder()
                .id(1L)
                .name("Test1")
                .description("Test1")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        practices.add(practice3);
    }

    @Test
    void getPracticesByUser(){

        when(repository.findByCreatorId(1L)).thenReturn(practices);

        getPracticesByUserUseCase.getPracticesByUser(1L);

        verify(repository, times(1)).findByCreatorId(1L);
        verify(streakValidator, times(3)).validateStreak(any(Practice.class));

        assertEquals(3,  practices.size());
    }
}