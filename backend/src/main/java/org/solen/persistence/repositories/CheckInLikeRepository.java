package org.solen.persistence.repositories;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInLikeRepository;
import org.solen.domain.checkin.CheckInLike;
import org.solen.persistence.converters.CheckInLikeConverter;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.entities.CheckInLikeEntity;
import org.solen.persistence.entities.UserEntity;
import org.solen.persistence.jparepos.CheckInJpaRepository;
import org.solen.persistence.jparepos.CheckInLikeJpaRepository;
import org.solen.persistence.jparepos.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CheckInLikeRepository implements ICheckInLikeRepository {

    private CheckInLikeConverter converter;
    private CheckInLikeJpaRepository jpaRepository;
    private CheckInJpaRepository checkInJpaRepository;
    private UserJpaRepository userJpaRepository;

    @Override
    public Optional<CheckInLike> findByCheckInIdAndUserId(Long checkInId, Long userId) {
        return jpaRepository.findByCheckInIdAndUserId(checkInId, userId)
                .map(converter::convertToDomain);
    }

    @Override
    public CheckInLike save(CheckInLike checkInLike) {
        CheckInEntity checkInEntity = checkInJpaRepository.getReferenceById(checkInLike.getCheckInId());
        UserEntity userEntity = userJpaRepository.getReferenceById(checkInLike.getUserId());
        CheckInLikeEntity entity = converter.convertToEntity(checkInLike, checkInEntity, userEntity);
        return converter.convertToDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(CheckInLike checkInLike) {
        jpaRepository.deleteById(checkInLike.getId());
    }

    @Override
    public int countByCheckInId(Long checkInId) {
        return jpaRepository.countByCheckInId(checkInId);
    }

    @Override
    public List<Long> findCheckInIdsLikedByUser(Long userId, List<Long> checkInIds) {
        if (checkInIds.isEmpty()) return List.of();
        return jpaRepository.findCheckInIdsLikedByUser(userId, checkInIds);
    }

    @Override
    public Map<Long, Integer> countByCheckInIds(List<Long> checkInIds) {
        if (checkInIds.isEmpty()) return Map.of();
        return jpaRepository.countByCheckInIds(checkInIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).intValue()
                ));
    }
}
