package org.solen.business.usercases;

import org.solen.controller.dto.user.UpdateUserRequest;
import org.solen.domain.users.User;

public interface UpdateUserUseCase {
    User updateUser(UpdateUserRequest request, Long id);
}