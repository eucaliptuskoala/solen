package org.solen.business.checkin.fypstrategy;

import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
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

        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(Practice.builder().id(1L).build()));
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(practiceNameBased.findPublicCheckIns(1L)).thenReturn(List.of(ci));

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(practiceNameBased).findPublicCheckIns(1L);
        verify(defaultStrategy, never()).findPublicCheckIns(anyLong());
    }

    @Test
    void findPublicCheckIns_userHasNoPractices_usesDefault() {
        service = new RecommendationService(practiceNameBased, defaultStrategy, practiceRepository);

        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of());
        CheckIn ci = CheckIn.builder().id(1L).build();
        when(defaultStrategy.findPublicCheckIns(1L)).thenReturn(List.of(ci));

        List<CheckIn> result = service.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(defaultStrategy).findPublicCheckIns(1L);
        verify(practiceNameBased, never()).findPublicCheckIns(anyLong());
    }
}
