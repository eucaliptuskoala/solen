package org.solen.business.practicecases.creationstrategy;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.exceptions.PracticeAlreadyExistsException;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;

import static org.solen.business.practicecases.creationstrategy.NameUtils.normalizeName;
import org.springframework.stereotype.Service;

import java.util.List;

// Strategy: creates a practice linked to an existing category.
// Used when the client provides a categoryId in the create request.
@Service("categoryCreationStrategy")
@AllArgsConstructor
public class CategoryPracticeCreationStrategy implements IPracticeCreationStrategy {

    private ICategoryRepository categoryRepository;
    private IPracticeRepository practiceRepository;
    private IUserRepository userRepository;

    @Override
    public Practice createPractice(Long categoryId, String name, String description, Long userId) {
        // Look up the category the user wants to tag this practice with
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundByIdException(categoryId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));

        // Prevent duplicate practice names for the same user
        List<Practice> existingPractices = practiceRepository.findByCreatorId(user.getId());
        String newPracticeName = normalizeName(name);

        if(existingPractices.stream().anyMatch(practice -> practice.getName().equals(newPracticeName))) {
            throw new PracticeAlreadyExistsException();
        }

        return practiceRepository.save(Practice.builder()
                .name(newPracticeName)
                .description(description)
                .streak(0)
                .thresholdDays(1)
                .creator(user)
                .category(category)
                .build());
    }
}
