package org.solen.business.usercases;

import org.solen.controller.dto.user.CreateUserRequest;
import org.solen.domain.users.User;

public interface CreateUserUseCase {
    User createUser(CreateUserRequest request);
}