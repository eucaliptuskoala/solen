package org.solen.configuration.security.ownership;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@AllArgsConstructor
public class UserSecurity {

    private IUserRepository userRepository;

    public boolean isOwnerOrAdmin(Long userId, String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return user.isAdmin() || user.getId().equals(userId);
    }
}
