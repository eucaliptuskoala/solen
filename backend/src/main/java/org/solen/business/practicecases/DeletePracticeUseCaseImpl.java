package org.solen.business.practicecases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.PracticeNotFoundByIdException;
import org.solen.business.repos.IPracticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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