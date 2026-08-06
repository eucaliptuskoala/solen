package org.solen.business.checkincases.fypstrategy;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInRepository;
import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Service;

import java.util.List;

// Personalised FYP strategy: recommends public check-ins from practices that share
// at least one category with the user's own practices. Excludes the user's own entries.
//
// The caller (RecommendationService) derives the distinct category IDs from the user's
// practices and passes them in; the category matching and own-entry exclusion happen in SQL.
@Service("practiceNameBased")
@AllArgsConstructor
public class PracticeBasedRecommendation implements IRecommendationStrategy {

    private ICheckInRepository checkInRepository;

    @Override
    public List<CheckIn> findPublicCheckIns(Long userId, List<Long> categoryIds) {
        return checkInRepository.findPublicCheckInsForCategories(categoryIds, userId);
    }
}
