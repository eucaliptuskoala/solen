package org.solen.business.practicecases;

import org.solen.business.practicecases.creationstrategy.PracticeCreationStrategyService;
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

    Long categoryId;
    String name;
    String description;
    Long userId;
    Practice practice;

    @BeforeEach
    void setUp() {
        useCase = new CreatePracticeUseCaseImpl(strategyService);
        categoryId = null;
        name = "practice";
        description = "desc";
        userId = 1L;
        practice = Practice.builder().name("practice").build();
    }

    @Test
    void createPractice_delegatesToStrategyService() {
        when(strategyService.getStrategy(categoryId, name, description, userId)).thenReturn(practice);

        Practice result = useCase.createPractice(categoryId, name, description, userId);

        verify(strategyService, times(1)).getStrategy(categoryId, name, description, userId);
        assertEquals(practice, result);
    }
}