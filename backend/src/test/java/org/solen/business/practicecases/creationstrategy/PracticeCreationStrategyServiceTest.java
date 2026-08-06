package org.solen.business.practicecases.creationstrategy;

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
    Long categoryId;
    Long noCategoryId;
    String name;
    String description;
    Practice practiceCategory;
    Practice practiceCustom;

    @BeforeEach
    void setUp() {

        strategyService = new PracticeCreationStrategyService(categoryStrategy, customStrategy);

        practiceCategory = Practice.builder().name("categoryPractice").build();
        practiceCustom = Practice.builder().name("customPractice").build();

        userId = 1L;
        categoryId = 1L;
        noCategoryId = null;
        name = "practice";
        description = "desc";
    }

    @Test
    void getStrategy_usesCategoryStrategy() {
        when(categoryStrategy.createPractice(categoryId, name, description, userId)).thenReturn(practiceCategory);

        Practice result = strategyService.getStrategy(categoryId, name, description, userId);

        verify(categoryStrategy, times(1)).createPractice(categoryId, name, description, userId);
        verify(customStrategy, never()).createPractice(any(), any(), any(), any());
        assertEquals(practiceCategory, result);
    }

    @Test
    void getStrategy_usesCustomStrategy() {
        when(customStrategy.createPractice(noCategoryId, name, description, userId)).thenReturn(practiceCustom);

        Practice result = strategyService.getStrategy(noCategoryId, name, description, userId);

        verify(customStrategy, times(1)).createPractice(noCategoryId, name, description, userId);
        verify(categoryStrategy, never()).createPractice(any(), any(), any(), any());
        assertEquals(practiceCustom, result);
    }

}