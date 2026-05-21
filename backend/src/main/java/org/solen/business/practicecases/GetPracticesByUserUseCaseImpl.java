package org.solen.business.practicecases;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.practices.Practice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetPracticesByUserUseCaseImpl implements IGetPracticesByUserUseCase {

    private IPracticeRepository repository;
    private StreakValidator validator;

    @Override
    public List<Practice> getPracticesByUser(Long userId) {
        List<Practice> practices = repository.findByCreatorId(userId);
        for(Practice practice : practices){
            validator.validateStreak(practice);
        }
        return practices;
    }
}