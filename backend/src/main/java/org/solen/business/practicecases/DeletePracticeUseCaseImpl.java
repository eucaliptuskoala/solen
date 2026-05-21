package org.solen.business.practicecases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.repos.IPracticeRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DeletePracticeUseCaseImpl implements IDeletePracticeUseCase {

    private IPracticeRepository repository;

    @Override
    public void deletePractice(Long id) {
        if (repository.findById(id) == null) {
            throw new PracticeNotFoundByIdException(id);
        }
        repository.deleteById(id);
    }
}