package org.solen.business.checkincases.fypstrategy;

import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private IRecommendationStrategy practiceNameBased;

    @Mock
    private IRecommendationStrategy defaultStrategy;

    @Mock
    private IPracticeRepository practiceRepository;

    private RecommendationService service;

    @Test
    void findPublicCheckIns_userHasPractices_usesPracticeBased() {
        service = new RecommendationService(practiceNameBased, defaultStrategy, practiceRepository);

        Practice practice = Practice.builder()
                .id(1L)
                .category(Category.builder().id(1L).build())
                .build();
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(practice));
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(practiceNameBased.findPublicCheckIns(1L, List.of(1L))).thenReturn(List.of(ci));

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(practiceRepository, times(1)).findByCreatorId(1L);
        verify(practiceNameBased).findPublicCheckIns(1L, List.of(1L));
        verify(defaultStrategy, never()).findPublicCheckIns(anyLong(), anyList());
    }

    @Test
    void findPublicCheckIns_passesDistinctNonNullCategoryIds() {
        service = new RecommendationService(practiceNameBased, defaultStrategy, practiceRepository);

        List<Practice> practices = List.of(
                Practice.builder().id(1L).category(Category.builder().id(1L).build()).build(),
                Practice.builder().id(2L).category(Category.builder().id(1L).build()).build(),
                Practice.builder().id(3L).category(null).build()
        );
        when(practiceRepository.findByCreatorId(1L)).thenReturn(practices);
        when(practiceNameBased.findPublicCheckIns(1L, List.of(1L))).thenReturn(List.of());

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
        verify(practiceRepository, times(1)).findByCreatorId(1L);
        verify(practiceNameBased).findPublicCheckIns(1L, List.of(1L));
    }

    @Test
    void findPublicCheckIns_onlyUncategorizedPractices_usesPracticeBasedWithEmptyCategoryIds() {
        service = new RecommendationService(practiceNameBased, defaultStrategy, practiceRepository);

        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(Practice.builder().id(1L).build()));
        when(practiceNameBased.findPublicCheckIns(1L, List.of())).thenReturn(List.of());

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
        verify(practiceNameBased).findPublicCheckIns(1L, List.of());
        verify(defaultStrategy, never()).findPublicCheckIns(anyLong(), anyList());
    }

    @Test
    void findPublicCheckIns_userHasNoPractices_usesDefault() {
        service = new RecommendationService(practiceNameBased, defaultStrategy, practiceRepository);

        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of());
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(defaultStrategy.findPublicCheckIns(1L, List.of())).thenReturn(List.of(ci));

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(practiceRepository, times(1)).findByCreatorId(1L);
        verify(defaultStrategy).findPublicCheckIns(1L, List.of());
        verify(practiceNameBased, never()).findPublicCheckIns(anyLong(), anyList());
    }
}
