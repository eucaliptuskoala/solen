package org.solen.business.checkincases;

import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;

public interface IUpdateCheckInUseCase {
    CheckIn update(Long id, String content, boolean isPublic, Mood mood);
}
