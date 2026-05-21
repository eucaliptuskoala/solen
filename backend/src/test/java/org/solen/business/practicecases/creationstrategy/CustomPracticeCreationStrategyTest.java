package org.solen.business.practicecases.creationstrategy;

import org.solen.business.exceptions.PracticeAlreadyExistsException;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IPracticeRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.controller.dto.practice.CreatePracticeRequest;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomPracticeCreationStrategyTest {

    @Mock
    IPracticeRepository practiceRepository;

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    CustomPracticeCreationStrategy customPracticeCreationStrategy;

    CreatePracticeRequest createPracticeRequest;
    Long userId;
    User user;
    Practice practice;
    List<Practice> existingPractices;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user =  User.builder()
                .id(1L)
                .name("test1")
                .email("test@mail.com")
                .password("12345")
                .isAdmin(false)
                .build();

        createPracticeRequest = CreatePracticeRequest.builder()
                .name("Test1")
                .description("Test1")
                .build();

        practice = Practice.builder()
                .name(createPracticeRequest.getName())
                .description(createPracticeRequest.getDescription())
                .streak(0)
                .lastUpdatedStreak(null)
                .thresholdDays(1)
                .creator(user)
                .build();

        existingPractices = new ArrayList<>();

        Practice practice1 = Practice.builder()
                .id(1L)
                .name("Test2")
                .description("Test2")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        existingPractices.add(practice1);

        Practice practice2 = Practice.builder()
                .id(2L)
                .name("Test2")
                .description("Test2")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        existingPractices.add(practice2);

        Practice practice3 = Practice.builder()
                .id(3L)
                .name("Test3")
                .description("Test3")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        existingPractices.add(practice3);

        Practice practice4 = Practice.builder()
                .id(3L)
                .name("Test1")
                .description("Test1")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .creator(user)
                .build();
        existingPractices.add(practice4);
    }

    @Test
    void createPractice_success(){
        when(userRepository.findById(userId)).thenReturn(user);
        when(practiceRepository.findByCreatorId(user.getId())).thenReturn(existingPractices.stream().limit(3).toList());

        customPracticeCreationStrategy.createPractice(createPracticeRequest, userId);

        verify(userRepository, times(1)).findById(userId);
        verify(practiceRepository, times(1)).save(practice);

        assertEquals("Test1", practice.getName());
    }

    @Test
    void createPractice_userNotFound()
    {
        when(userRepository.findById(userId)).thenReturn(null);

        UserNotFoundByIdException exception = assertThrows(UserNotFoundByIdException.class, ()-> customPracticeCreationStrategy.createPractice(createPracticeRequest, userId));

        assertEquals("User with id 1 does not exist", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(practiceRepository);
    }

    @Test
    void createPractice_nameAlreadyExists(){
        when(userRepository.findById(userId)).thenReturn(user);
        when(practiceRepository.findByCreatorId(user.getId())).thenReturn(existingPractices);

        PracticeAlreadyExistsException exception = assertThrows(PracticeAlreadyExistsException.class, ()-> customPracticeCreationStrategy.createPractice(createPracticeRequest, userId));

        assertEquals("Practice Already Exists!", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(practiceRepository, never()).save(practice);
    }
}