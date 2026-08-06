package org.solen.business.checkin.fypstrategy;

import org.solen.domain.checkin.CheckIn;

import java.util.List;

public interface IRecommendationStrategy {
    List<CheckIn> findPublicCheckIns(Long userId);
}
