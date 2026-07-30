package org.solen.business.usercases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.EmailAlreadyExistsException;
import org.solen.business.repos.IUserRepository;
import org.solen.controller.dto.user.CreateUserRequest;
import org.solen.domain.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private IUserRepository repository;
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest request) {

        if(request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if(repository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        else{
            return repository.save(User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .isAdmin(false)
                    .build());
        }
    }
}