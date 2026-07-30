package org.solen.business.usercases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.IUserRepository;
import org.solen.controller.dto.user.UpdateUserRequest;
import org.solen.domain.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private IUserRepository repository;
    private PasswordEncoder passwordEncoder;

    @Override
    public User updateUser(UpdateUserRequest request, Long id) {

        User user = repository.findById(id);

        if (user == null) {
            throw new UserNotFoundByIdException(id);
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }
}