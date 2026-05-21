package org.solen.business.practicecases.creationstrategy;

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
class PracticeCreationStrategyServiceTest {

    @Mock
    private IPracticeCreationStrategy categoryStrategy;

    @Mock
    private IPracticeCreationStrategy customStrategy;

    private PracticeCreationStrategyService strategyService;

    Long userId;
    CreatePracticeRequest requestWithCategory;
    CreatePracticeRequest requestCustom;
    Practice practiceCategory;
    Practice practiceCustom;

    @BeforeEach
    void setUp() {

        strategyService = new PracticeCreationStrategyService(categoryStrategy, customStrategy);

        practiceCategory = Practice.builder().name("categoryPractice").build();
        practiceCustom = Practice.builder().name("customPractice").build();

        userId = 1L;

        requestWithCategory = CreatePracticeRequest.builder()
                .categoryId(1L)
                .description("desc")
                .build();

        requestCustom = CreatePracticeRequest.builder()
                .categoryId(null)
                .description("desc")
                .build();
    }

    @Test
    void getStrategy_usesCategoryStrategy() {
        when(categoryStrategy.createPractice(requestWithCategory, userId)).thenReturn(practiceCategory);

        Practice result = strategyService.getStrategy(requestWithCategory, userId);

        verify(categoryStrategy, times(1)).createPractice(requestWithCategory, userId);
        verify(customStrategy, never()).createPractice(any(), any());
        assertEquals(practiceCategory, result);
    }

    @Test
    void getStrategy_usesCustomStrategy() {
        when(customStrategy.createPractice(requestCustom, userId)).thenReturn(practiceCustom);

        Practice result = strategyService.getStrategy(requestCustom, userId);

        verify(customStrategy, times(1)).createPractice(requestCustom, userId);
        verify(categoryStrategy, never()).createPractice(any(), any());
        assertEquals(practiceCustom, result);
    }

}