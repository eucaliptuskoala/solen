package org.solen.business.usercases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GetUserByIdUseCaseImpl implements IGetUserByIdUseCase {

    private IUserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException(id));
    }
}