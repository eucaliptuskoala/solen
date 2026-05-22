package org.solen.business.checkin;

public interface IToggleCheckInLikeUseCase {
    ToggleLikeResult toggle(Long checkInId, Long userId);
}
