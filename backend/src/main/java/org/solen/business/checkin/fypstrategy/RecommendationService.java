package org.solen.business.checkin.fypstrategy;

import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.practices.Practice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

// Router for FYP recommendation strategies.
//   user has practices → PracticeBasedRecommendation (category-matched, personalised)
//   user has no practices → DefaultRecommendationStrategy (all public, cold-start)
@Service
public class RecommendationService {

    @Qualifier("practiceNameBased")
    private final IRecommendationStrategy practiceNameBased;

    @Qualifier("default")
    private final IRecommendationStrategy defaultStrategy;

    private final IPracticeRepository practiceRepository;

    public RecommendationService(
            @Qualifier("practiceNameBased") IRecommendationStrategy practiceNameBased,
            @Qualifier("default") IRecommendationStrategy defaultStrategy,
            IPracticeRepository practiceRepository
    ) {
        this.practiceNameBased = practiceNameBased;
        this.defaultStrategy = defaultStrategy;
        this.practiceRepository = practiceRepository;
    }

    public List<CheckIn> findPublicCheckIns(Long userId) {
        List<Practice> userPractices = practiceRepository.findByCreatorId(userId);

        if (userPractices.isEmpty()) {
            return defaultStrategy.findPublicCheckIns(userId);
        } else {
            return practiceNameBased.findPublicCheckIns(userId);
        }
    }
}
