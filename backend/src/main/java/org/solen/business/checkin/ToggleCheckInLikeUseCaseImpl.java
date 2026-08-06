package org.solen.business.checkin;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.CheckInNotFoundException;
import org.solen.business.exceptions.SelfLikeNotAllowedException;
import org.solen.business.repos.ICheckInLikeRepository;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.CheckInLike;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToggleCheckInLikeUseCaseImpl implements IToggleCheckInLikeUseCase {

    private ICheckInRepository checkInRepository;
    private ICheckInLikeRepository likeRepository;

    @Override
    @Transactional
    public ToggleLikeResult toggle(Long checkInId, Long userId) {
        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new CheckInNotFoundException(checkInId));

        if (checkIn.getPractice().getCreator().getId().equals(userId)) {
            throw new SelfLikeNotAllowedException();
        }

        Optional<CheckInLike> existing = likeRepository.findByCheckInIdAndUserId(checkInId, userId);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
        } else {
            likeRepository.save(CheckInLike.builder()
                    .checkInId(checkInId)
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        int count = likeRepository.countByCheckInId(checkInId);
        return new ToggleLikeResult(!existing.isPresent(), count);
    }
}
