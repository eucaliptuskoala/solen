package org.solen.business.usercases;

import org.solen.domain.users.User;

public interface IPromoteToAdminUseCase {
    User promote(Long userId);
}
