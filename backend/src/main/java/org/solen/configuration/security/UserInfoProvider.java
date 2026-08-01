package org.solen.configuration.security;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UnauthorizedAccessException;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInfoProvider {

    private IUserRepository userRepository;

    public Long getUserId() {
        String email = getAuthenticatedEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundByEmailException(email);
        }
        return user.getId();
    }

    public String getUserEmail() {
        return getAuthenticatedEmail();
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedAccessException();
        }
        return authentication.getName();
    }
}
