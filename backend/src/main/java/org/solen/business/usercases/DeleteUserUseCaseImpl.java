package org.solen.business.usercases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class DeleteUserUseCaseImpl implements IDeleteUserUseCase {

    private final IUserRepository repository;

    @Override
    public void deleteUser(Long id) {
        if(repository.existsById(id)) {
            repository.deleteById(id);
        }
        else {
            throw new UserNotFoundByIdException(id);
        }
    }
}