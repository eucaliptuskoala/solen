package org.solen.configuration.security.ownership;

import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategorySecurityTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CategorySecurity categorySecurity;

    @Test
    void isAdminByEmail_userIsAdmin_returnsTrue() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(
                User.builder().id(1L).email("admin@test.com").isAdmin(true).build());

        assertTrue(categorySecurity.isAdminByEmail("admin@test.com"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(
                User.builder().id(2L).email("user@test.com").isAdmin(false).build());

        assertFalse(categorySecurity.isAdminByEmail("user@test.com"));
    }

    @Test
    void isAdminByEmail_userNotFound_returnsFalse() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(null);

        assertFalse(categorySecurity.isAdminByEmail("unknown@test.com"));
    }
}
