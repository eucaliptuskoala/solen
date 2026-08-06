package org.solen.business.usercases;

import org.solen.domain.users.User;

public interface GetUserByIdUseCase {
    User getUserById(Long id);
}