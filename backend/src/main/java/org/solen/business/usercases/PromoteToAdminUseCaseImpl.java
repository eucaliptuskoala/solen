package org.solen.business.usercases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PromoteToAdminUseCaseImpl implements IPromoteToAdminUseCase {

    private IUserRepository repository;

    @Override
    public User promote(Long userId) {
        User user = repository.findById(userId);
        if (user == null) {
            throw new UserNotFoundByIdException(userId);
        }
        user.setAdmin(true);
        return repository.save(user);
    }
}
