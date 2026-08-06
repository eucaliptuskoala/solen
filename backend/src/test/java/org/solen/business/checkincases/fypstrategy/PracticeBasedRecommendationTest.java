package org.solen.business.checkincases.fypstrategy;

import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticeBasedRecommendationTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @InjectMocks
    private PracticeBasedRecommendation recommendation;

    private CheckIn makePublicCheckIn(Long id) {
        return CheckIn.builder().id(id).build();
    }

    @Test
    void findPublicCheckIns_delegatesWithCategoryIds() {
        CheckIn matched = makePublicCheckIn(1L);
        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of(matched));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L, List.of(1L));

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }

    @Test
    void findPublicCheckIns_usesUserIdForOwnExclusion() {
        CheckIn any = makePublicCheckIn(1L);
        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of(any));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L, List.of(1L));

        assertEquals(1, result.size());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }

    @Test
    void findPublicCheckIns_emptyCategoryIds_returnsEmpty() {
        when(checkInRepository.findPublicCheckInsForCategories(List.of(), 1L)).thenReturn(List.of());

        List<CheckIn> result = recommendation.findPublicCheckIns(1L, List.of());

        assertTrue(result.isEmpty());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(), 1L);
    }

    @Test
    void findPublicCheckIns_noMatches_returnsEmpty() {
        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of());

        List<CheckIn> result = recommendation.findPublicCheckIns(1L, List.of(1L));

        assertTrue(result.isEmpty());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }
}
