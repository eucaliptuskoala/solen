package org.solen.persistence.converters;

import lombok.AllArgsConstructor;
import org.solen.domain.practices.Practice;
import org.solen.persistence.entities.CategoryEntity;
import org.solen.persistence.entities.PracticeEntity;
import org.solen.persistence.entities.UserEntity;
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
                .category(practice.getCategory() != null
                        ? CategoryEntity.builder().id(practice.getCategory().getId()).build()
                        : null)
                .creator(practice.getCreator() != null
                        ? UserEntity.builder().id(practice.getCreator().getId()).build()
                        : null)
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