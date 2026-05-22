package org.solen.persistence.converters;

import org.solen.domain.checkin.CheckInLike;
import org.solen.persistence.entities.CheckInLikeEntity;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CheckInLikeConverter {

    public CheckInLikeEntity convertToEntity(CheckInLike checkInLike, CheckInEntity checkInEntity, UserEntity userEntity) {
        if (checkInLike == null) return null;
        return CheckInLikeEntity.builder()
                .id(checkInLike.getId())
                .checkIn(checkInEntity)
                .user(userEntity)
                .createdAt(checkInLike.getCreatedAt())
                .build();
    }

    public CheckInLike convertToDomain(CheckInLikeEntity entity) {
        if (entity == null) return null;
        return CheckInLike.builder()
                .id(entity.getId())
                .checkInId(entity.getCheckIn().getId())
                .userId(entity.getUser().getId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
