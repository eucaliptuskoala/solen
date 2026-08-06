package org.solen.business.usercases;

import org.solen.domain.users.User;

public interface IGetUserByIdUseCase {
    User getUserById(Long id);
}