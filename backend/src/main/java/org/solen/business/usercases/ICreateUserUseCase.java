package org.solen.business.usercases;

import org.solen.controller.dto.user.CreateUserRequest;
import org.solen.domain.users.User;

public interface ICreateUserUseCase {
    User createUser(CreateUserRequest request);
}