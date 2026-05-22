package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInLikeRepository;
import org.solen.controller.dto.checkin.CheckInDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CheckInLikeEnricher {

    private ICheckInLikeRepository likeRepository;

    public void enrich(List<CheckInDto> dtos, Long userId) {
        if (dtos.isEmpty()) return;

        List<Long> checkInIds = dtos.stream().map(CheckInDto::getId).toList();
        Map<Long, Integer> counts = likeRepository.countByCheckInIds(checkInIds);
        Set<Long> likedIds = new HashSet<>(likeRepository.findCheckInIdsLikedByUser(userId, checkInIds));

        dtos.forEach(dto -> {
            dto.setLikeCount(counts.getOrDefault(dto.getId(), 0));
            dto.setLikedByCurrentUser(likedIds.contains(dto.getId()));
        });
    }
}
