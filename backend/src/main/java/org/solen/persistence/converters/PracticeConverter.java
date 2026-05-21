package org.solen.persistence.converters;

import lombok.AllArgsConstructor;
import org.solen.domain.practices.Practice;
import org.solen.persistence.entities.PracticeEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PracticeConverter {

    private UserConverter userConverter;
    private CategoryConverter categoryConverter;

    public PracticeEntity convertToEntity(Practice practice) {
        return PracticeEntity.builder()
                .id(practice.getId())
                .name(practice.getName())
                .description(practice.getDescription())
                .streak(practice.getStreak())
                .lastUpdatedStreak(practice.getLastUpdatedStreak())
                .thresholdDays(practice.getThresholdDays())
                .category(categoryConverter.convertToEntity(practice.getCategory()))
                .creator(userConverter.convertToEntity(practice.getCreator()))
                .build();
    }

    public Practice convertToDomain(PracticeEntity entity) {
        return Practice.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .streak(entity.getStreak())
                .lastUpdatedStreak(entity.getLastUpdatedStreak())
                .thresholdDays(entity.getThresholdDays())
                .category(categoryConverter.convertToDomain(entity.getCategory()))
                .creator(userConverter.convertToDomain(entity.getCreator()))
                .build();
    }
}