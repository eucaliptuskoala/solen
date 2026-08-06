package org.solen.business.checkin.fypstrategy;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// Personalised FYP strategy: recommends public check-ins from practices that share
// at least one category with the user's own practices. Excludes the user's own entries.
//
// Flow:
//   1. Collect all category IDs from the user's practices
//   2. Fetch public check-ins matching those categories, excluding the user's own entries (done in SQL)
@Service("practiceNameBased")
@AllArgsConstructor
public class PracticeBasedRecommendation implements IRecommendationStrategy {

    private ICheckInRepository checkInRepository;
    private IPracticeRepository practiceRepository;

    @Override
    public List<CheckIn> findPublicCheckIns(Long userId) {
        List<Long> userCategoryIds = practiceRepository.findByCreatorId(userId).stream()
                .map(practice -> practice.getCategory() != null ? practice.getCategory().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return checkInRepository.findPublicCheckInsForCategories(userCategoryIds, userId);
    }
}
