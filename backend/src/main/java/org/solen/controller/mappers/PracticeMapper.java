package org.solen.controller.mappers;

import org.solen.controller.dto.practice.PracticeDto;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PracticeMapper {

    public PracticeDto convertToDto(Practice practice) {
        return convertToDto(practice, Set.of());
    }

    public PracticeDto convertToDto(Practice practice, Set<Long> checkedInTodayIds) {
        return PracticeDto.builder()
                .id(practice.getId())
                .name(practice.getName())
                .description(practice.getDescription())
                .streak(practice.getStreak())
                .lastUpdatedStreak(practice.getLastUpdatedStreak())
                .thresholdDays(practice.getThresholdDays())
                .categoryId(practice.getCategory() != null ? practice.getCategory().getId() : null)
                .categoryName(practice.getCategory() != null ? practice.getCategory().getName() : null)
                .checkedInToday(checkedInTodayIds.contains(practice.getId()))
                .build();
    }
}