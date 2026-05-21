package org.solen.controller.mappers;

import org.solen.controller.dto.user.UserDto;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper mapper;

    @Test
    void convertToDto_mapsAllFields() {
        User user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@test.com")
                .password("secret")
                .isAdmin(true)
                .build();

        UserDto dto = mapper.convertToDto(user);

        assertEquals(1L, dto.getId());
        assertEquals("Alice", dto.getName());
        assertEquals("alice@test.com", dto.getEmail());
        assertTrue(dto.isAdmin());
    }
}
