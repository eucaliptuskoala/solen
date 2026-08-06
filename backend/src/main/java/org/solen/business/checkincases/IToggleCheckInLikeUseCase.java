package org.solen.business.checkincases;

public interface IToggleCheckInLikeUseCase {
    ToggleLikeResult toggle(Long checkInId, Long userId);
}
