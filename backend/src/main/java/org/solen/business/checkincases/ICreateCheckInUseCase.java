package org.solen.business.checkincases;

import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;

public interface ICreateCheckInUseCase {
    CheckIn create(Long practiceId, Long userId);
    CheckIn createWithDetails(Long practiceId, String content, boolean isPublic, Mood mood, Long userId);
}
