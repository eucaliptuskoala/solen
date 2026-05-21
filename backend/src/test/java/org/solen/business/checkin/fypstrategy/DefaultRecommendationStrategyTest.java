package org.solen.business.checkin.fypstrategy;

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
class DefaultRecommendationStrategyTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @InjectMocks
    private DefaultRecommendationStrategy strategy;

    @Test
    void findPublicCheckIns_returnsAllPublicCheckIns() {
        CheckIn ci1 = CheckIn.builder().id(1L).build();
        CheckIn ci2 = CheckIn.builder().id(2L).build();
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(ci1, ci2));

        List<CheckIn> result = strategy.findPublicCheckIns(1L);

        assertEquals(2, result.size());
        verify(checkInRepository).findPublicCheckIns();
    }

    @Test
    void findPublicCheckIns_returnsEmpty_whenNone() {
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of());

        List<CheckIn> result = strategy.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
        verify(checkInRepository).findPublicCheckIns();
    }
}
