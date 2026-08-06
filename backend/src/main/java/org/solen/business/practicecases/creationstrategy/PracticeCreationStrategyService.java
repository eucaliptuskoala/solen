package org.solen.business.practicecases.creationstrategy;

import org.solen.domain.practices.Practice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

// Router that selects the correct IPracticeCreationStrategy based on the request.
// Not a factory — strategies are injected by @Qualifier bean name.
//   categoryId present → CategoryPracticeCreationStrategy (links to a category)
//   categoryId null    → CustomPracticeCreationStrategy     (free-form practice)
@Service
public class PracticeCreationStrategyService {

    @Qualifier("categoryCreationStrategy")
    private final IPracticeCreationStrategy categoryStrategy;

    @Qualifier("customCreationStrategy")
    private final IPracticeCreationStrategy custom;

    public PracticeCreationStrategyService(
            @Qualifier("categoryCreationStrategy") IPracticeCreationStrategy categoryStrategy,
            @Qualifier("customCreationStrategy") IPracticeCreationStrategy custom
    ) {
        this.categoryStrategy = categoryStrategy;
        this.custom = custom;
    }

    public Practice getStrategy (Long categoryId, String name, String description, Long userId){
        if(categoryId == null){
            return custom.createPractice(categoryId, name, description, userId);
        }
        else{
            return categoryStrategy.createPractice(categoryId, name, description, userId);
        }
    }
}