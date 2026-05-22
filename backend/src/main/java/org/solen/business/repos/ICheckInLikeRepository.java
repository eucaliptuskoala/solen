package org.solen.business.repos;

import org.solen.domain.checkin.CheckInLike;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICheckInLikeRepository {
    Optional<CheckInLike> findByCheckInIdAndUserId(Long checkInId, Long userId);
    CheckInLike save(CheckInLike checkInLike);
    void delete(CheckInLike checkInLike);
    int countByCheckInId(Long checkInId);
    List<Long> findCheckInIdsLikedByUser(Long userId, List<Long> checkInIds);
    Map<Long, Integer> countByCheckInIds(List<Long> checkInIds);
}
