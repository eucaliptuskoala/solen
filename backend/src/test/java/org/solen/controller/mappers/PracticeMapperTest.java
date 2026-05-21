package org.solen.controller.mappers;

import org.solen.controller.dto.practice.PracticeDto;
import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PracticeMapperTest {

    @InjectMocks
    private PracticeMapper mapper;

    @Test
    void convertToDto_mapsAllFields() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Read")
                .description("Read daily")
                .streak(5)
                .lastUpdatedStreak(LocalDateTime.of(2026, 5, 21, 10, 0))
                .thresholdDays(2)
                .category(Category.builder().id(1L).name("Reading").build())
                .build();

        PracticeDto dto = mapper.convertToDto(practice);

        assertEquals(1L, dto.getId());
        assertEquals("Read", dto.getName());
        assertEquals("Read daily", dto.getDescription());
        assertEquals(5, dto.getStreak());
        assertEquals(1L, dto.getCategoryId());
        assertEquals("Reading", dto.getCategoryName());
        assertFalse(dto.isCheckedInToday());
    }

    @Test
    void convertToDto_nullCategory_setsNullCategoryFields() {
        Practice practice = Practice.builder()
                .id(1L).name("Test").streak(0).thresholdDays(1)
                .category(null)
                .build();

        PracticeDto dto = mapper.convertToDto(practice);

        assertNull(dto.getCategoryId());
        assertNull(dto.getCategoryName());
    }

    @Test
    void convertToDto_withCheckedInTodayIds_marksCheckedIn() {
        Practice practice = Practice.builder()
                .id(1L).name("Test").streak(0).thresholdDays(1)
                .build();

        PracticeDto dto = mapper.convertToDto(practice, Set.of(1L));

        assertTrue(dto.isCheckedInToday());
    }

    @Test
    void convertToDto_withCheckedInTodayIds_notCheckedIn() {
        Practice practice = Practice.builder()
                .id(1L).name("Test").streak(0).thresholdDays(1)
                .build();

        PracticeDto dto = mapper.convertToDto(practice, Set.of(2L));

        assertFalse(dto.isCheckedInToday());
    }
}
