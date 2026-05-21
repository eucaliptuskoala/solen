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
//   2. Fetch all public check-ins globally
//   3. Keep only those whose practice's category matches the user's categories
//   4. Exclude check-ins the user created themselves
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

        return checkInRepository.findPublicCheckIns().stream()
                .filter(ci -> !ci.getPractice().getCreator().getId().equals(userId))
                .filter(ci -> ci.getPractice().getCategory() != null
                        && userCategoryIds.contains(ci.getPractice().getCategory().getId()))
                .toList();
    }
}
