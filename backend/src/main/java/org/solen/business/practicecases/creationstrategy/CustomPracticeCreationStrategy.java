package org.solen.business.practicecases.creationstrategy;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.PracticeAlreadyExistsException;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IPracticeRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;

import static org.solen.business.practicecases.creationstrategy.NameUtils.normalizeName;
import org.springframework.stereotype.Service;

import java.util.List;

// Strategy: creates a free-form practice without linking it to a category.
// Used when the client does NOT provide a categoryId in the create request.
// Identical logic to CategoryPracticeCreationStrategy but with category set to null.
@Service("customCreationStrategy")
@AllArgsConstructor
public class CustomPracticeCreationStrategy implements IPracticeCreationStrategy {

    private IPracticeRepository practiceRepository;
    private IUserRepository userRepository;

    @Override
    public Practice createPractice(CreatePracticeRequest request, Long userId) {

        User user = userRepository.findById(userId);

        if(user == null) {
            throw new UserNotFoundByIdException(userId);
        }

        List<Practice> existingPractices = practiceRepository.findByCreatorId(user.getId());

        String newPracticeName = normalizeName(request.getName());

        if(existingPractices.stream().anyMatch(practice -> practice.getName().equals(newPracticeName))) {
            throw new PracticeAlreadyExistsException();
        }

        return practiceRepository.save(Practice.builder()
                .name(newPracticeName)
                .description(request.getDescription())
                .streak(0)
                .thresholdDays(1)
                .creator(user)
                .category(null)
                .build());
    }
}