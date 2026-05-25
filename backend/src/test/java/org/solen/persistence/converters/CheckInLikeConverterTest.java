package org.solen.persistence.converters;

import org.solen.domain.checkin.CheckInLike;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.entities.CheckInLikeEntity;
import org.solen.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckInLikeConverterTest {

    @InjectMocks
    private CheckInLikeConverter converter;

    @Test
    void convertToEntity_null_returnsNull() {
        assertNull(converter.convertToEntity(null, null, null));
    }

    @Test
    void convertToDomain_null_returnsNull() {
        assertNull(converter.convertToDomain(null));
    }

    @Test
    void convertToEntity_mapsAllFields() {
        CheckInLike checkInLike = CheckInLike.builder()
                .id(1L)
                .checkInId(10L)
                .userId(20L)
                .createdAt(LocalDateTime.of(2026, 5, 22, 10, 0))
                .build();
        CheckInEntity checkInEntity = CheckInEntity.builder().id(10L).build();
        UserEntity userEntity = UserEntity.builder().id(20L).build();

        CheckInLikeEntity entity = converter.convertToEntity(checkInLike, checkInEntity, userEntity);

        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getCheckIn().getId());
        assertEquals(20L, entity.getUser().getId());
        assertEquals(LocalDateTime.of(2026, 5, 22, 10, 0), entity.getCreatedAt());
    }

    @Test
    void convertToDomain_mapsAllFields() {
        CheckInEntity checkInEntity = CheckInEntity.builder().id(10L).build();
        UserEntity userEntity = UserEntity.builder().id(20L).build();
        CheckInLikeEntity entity = CheckInLikeEntity.builder()
                .id(1L)
                .checkIn(checkInEntity)
                .user(userEntity)
                .createdAt(LocalDateTime.of(2026, 5, 22, 10, 0))
                .build();

        CheckInLike checkInLike = converter.convertToDomain(entity);

        assertEquals(1L, checkInLike.getId());
        assertEquals(10L, checkInLike.getCheckInId());
        assertEquals(20L, checkInLike.getUserId());
        assertEquals(LocalDateTime.of(2026, 5, 22, 10, 0), checkInLike.getCreatedAt());
    }

    @Test
    void roundTrip_success() {
        CheckInLike checkInLike = CheckInLike.builder()
                .id(1L)
                .checkInId(10L)
                .userId(20L)
                .createdAt(LocalDateTime.now())
                .build();
        CheckInEntity checkInEntity = CheckInEntity.builder().id(10L).build();
        UserEntity userEntity = UserEntity.builder().id(20L).build();

        CheckInLikeEntity entity = converter.convertToEntity(checkInLike, checkInEntity, userEntity);
        CheckInLike result = converter.convertToDomain(entity);

        assertEquals(checkInLike.getId(), result.getId());
        assertEquals(checkInLike.getCheckInId(), result.getCheckInId());
        assertEquals(checkInLike.getUserId(), result.getUserId());
    }
}
