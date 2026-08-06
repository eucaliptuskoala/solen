package org.solen.business.checkincases.fypstrategy;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Service;

import java.util.List;

// Fallback FYP strategy: returns ALL public check-ins with no filtering.
// Used when the user has no practices yet and therefore no category preferences to match on.
@Service("default")
@AllArgsConstructor
public class DefaultRecommendationStrategy implements IRecommendationStrategy {

    private ICheckInRepository checkInRepository;

    @Override
    public List<CheckIn> findPublicCheckIns(Long userId, List<Long> categoryIds) {
        return checkInRepository.findPublicCheckIns();
    }
}
