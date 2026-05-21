package org.solen.business.habitcases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.HabitNotFoundByIdException;
import org.solen.business.repos.IHabitRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DeleteHabitUseCaseImpl implements IDeleteHabitUseCase {

    private IHabitRepository repository;

    @Override
    public void deleteHabit(Long id) {
        if (repository.findById(id) == null) {
            throw new HabitNotFoundByIdException(id);
        }
        repository.deleteById(id);
    }
}