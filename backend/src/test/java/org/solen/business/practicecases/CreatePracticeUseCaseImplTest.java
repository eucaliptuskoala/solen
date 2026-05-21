package org.solen.business.practicecases;

import org.solen.business.practicecases.creationstrategy.PracticeCreationStrategyService;
import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.domain.practices.Practice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePracticeUseCaseImplTest {

    @Mock
    private PracticeCreationStrategyService strategyService;

    private CreatePracticeUseCaseImpl useCase;

    CreatePracticeRequest request;
    Long userId;
    Practice practice;

    @BeforeEach
    void setUp() {
        useCase = new CreatePracticeUseCaseImpl(strategyService);
        userId = 1L;
        request = CreatePracticeRequest.builder().description("desc").build();
        practice = Practice.builder().name("practice").build();
    }

    @Test
    void createPractice_delegatesToStrategyService() {
        when(strategyService.getStrategy(request, userId)).thenReturn(practice);

        Practice result = useCase.createPractice(request, userId);

        verify(strategyService, times(1)).getStrategy(request, userId);
        assertEquals(practice, result);
    }
}