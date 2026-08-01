package org.solen.configuration.security;

import org.solen.business.exceptions.UnauthorizedAccessException;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdProviderTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserInfoProvider userIdProvider;

    @Test
    void getUserId_success() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@test.com", "pwd"));
        when(userRepository.findByEmail("user@test.com")).thenReturn(
                User.builder().id(42L).email("user@test.com").build());

        Long userId = userIdProvider.getUserId();

        assertEquals(42L, userId);
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserId_userNotFound_throws() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("unknown@test.com", "pwd"));
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(null);

        assertThrows(UserNotFoundByEmailException.class, () -> userIdProvider.getUserId());
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserId_noAuthentication_throws() {
        SecurityContextHolder.clearContext();

        assertThrows(UnauthorizedAccessException.class, () -> userIdProvider.getUserId());
    }

    @Test
    void getUserEmail_noAuthentication_throws() {
        SecurityContextHolder.clearContext();

        assertThrows(UnauthorizedAccessException.class, () -> userIdProvider.getUserEmail());
    }
}
