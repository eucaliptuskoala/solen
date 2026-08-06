package org.solen.business.checkincases.fypstrategy;

import org.solen.domain.checkin.CheckIn;

import java.util.List;

public interface IGetForYouCheckInsUseCase {
    List<CheckIn> getForYouCheckIns(Long userId);
}
