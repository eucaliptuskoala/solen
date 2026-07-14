package org.solen.configuration.security;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInfoProvider {

    private IUserRepository userRepository;

    public Long getUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundByEmailException(email);
        }
        return user.getId();
    }

    public String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
