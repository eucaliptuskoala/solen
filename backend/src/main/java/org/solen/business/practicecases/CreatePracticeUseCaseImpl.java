package org.solen.business.practicecases;

import lombok.AllArgsConstructor;
import org.solen.business.practicecases.creationstrategy.PracticeCreationStrategyService;
import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatePracticeUseCaseImpl implements ICreatePracticeUseCase {

    private PracticeCreationStrategyService strategyService;

    @Override
    public Practice createPractice(CreatePracticeRequest request, Long userId) {
        return strategyService.getStrategy(request, userId);
    }
}