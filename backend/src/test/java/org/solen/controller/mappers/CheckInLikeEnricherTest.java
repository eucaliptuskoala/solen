package org.solen.controller.mappers;

import org.solen.business.repos.ICheckInLikeRepository;
import org.solen.controller.dto.checkin.CheckInDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInLikeEnricherTest {

    @Mock
    private ICheckInLikeRepository likeRepository;

    @InjectMocks
    private CheckInLikeEnricher enricher;

    @Test
    void enrich_populatesLikeCountsAndLikedStatus() {
        CheckInDto dto1 = CheckInDto.builder().id(1L).build();
        CheckInDto dto2 = CheckInDto.builder().id(2L).build();
        List<CheckInDto> dtos = List.of(dto1, dto2);

        when(likeRepository.countByCheckInIds(List.of(1L, 2L))).thenReturn(Map.of(1L, 5, 2L, 3));
        when(likeRepository.findCheckInIdsLikedByUser(10L, List.of(1L, 2L))).thenReturn(List.of(1L));

        enricher.enrich(dtos, 10L);

        assertEquals(5, dto1.getLikeCount());
        assertTrue(dto1.isLikedByCurrentUser());
        assertEquals(3, dto2.getLikeCount());
        assertFalse(dto2.isLikedByCurrentUser());
    }

    @Test
    void enrich_emptyList_doesNothing() {
        List<CheckInDto> dtos = List.of();

        enricher.enrich(dtos, 10L);

        verifyNoInteractions(likeRepository);
    }
}
