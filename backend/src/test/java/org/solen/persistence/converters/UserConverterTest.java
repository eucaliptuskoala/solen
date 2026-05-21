package org.solen.persistence.converters;

import org.solen.domain.users.User;
import org.solen.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

    @InjectMocks
    private UserConverter converter;

    @Test
    void convertToEntity_mapsAllFields() {
        User user = User.builder()
                .id(1L).name("Alice").email("a@t.com").password("secret").isAdmin(true)
                .build();

        UserEntity entity = converter.convertToEntity(user);

        assertEquals(1L, entity.getId());
        assertEquals("Alice", entity.getName());
        assertEquals("a@t.com", entity.getEmail());
        assertEquals("secret", entity.getPassword());
        assertTrue(entity.isAdmin());
    }

    @Test
    void convertToDomain_mapsAllFields() {
        UserEntity entity = UserEntity.builder()
                .id(1L).name("Alice").email("a@t.com").password("secret").isAdmin(true)
                .build();

        User user = converter.convertToDomain(entity);

        assertEquals(1L, user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("a@t.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    void convertToEntity_roundTrip() {
        User user = User.builder().id(1L).name("Bob").email("b@t.com").password("pwd").isAdmin(false).build();

        UserEntity entity = converter.convertToEntity(user);
        User back = converter.convertToDomain(entity);

        assertEquals(user.getId(), back.getId());
        assertEquals(user.getName(), back.getName());
        assertEquals(user.getEmail(), back.getEmail());
        assertEquals(user.getPassword(), back.getPassword());
        assertEquals(user.isAdmin(), back.isAdmin());
    }
}
