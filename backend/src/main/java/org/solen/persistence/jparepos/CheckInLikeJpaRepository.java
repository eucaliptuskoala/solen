package org.solen.persistence.jparepos;

import org.solen.persistence.entities.CheckInLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CheckInLikeJpaRepository extends JpaRepository<CheckInLikeEntity, Long> {

    Optional<CheckInLikeEntity> findByCheckInIdAndUserId(Long checkInId, Long userId);

    int countByCheckInId(Long checkInId);

    @Query("""
        select cl.checkIn.id
        from CheckInLikeEntity cl
        where cl.checkIn.id in :checkInIds and cl.user.id = :userId
    """)
    List<Long> findCheckInIdsLikedByUser(@Param("userId") Long userId, @Param("checkInIds") List<Long> checkInIds);

    @Query("""
        select cl.checkIn.id as checkInId, count(cl) as cnt
        from CheckInLikeEntity cl
        where cl.checkIn.id in :checkInIds
        group by cl.checkIn.id
    """)
    List<Object[]> countByCheckInIds(@Param("checkInIds") List<Long> checkInIds);
}
