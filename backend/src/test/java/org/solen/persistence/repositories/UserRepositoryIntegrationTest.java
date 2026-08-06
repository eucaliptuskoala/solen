package org.solen.persistence.repositories;

import org.solen.domain.users.User;
import org.solen.persistence.converters.UserConverter;
import org.solen.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UserRepository.class, UserConverter.class})
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .name("Test User")
                .email("test@mail.com")
                .password("password")
                .isAdmin(false)
                .build();
    }

    private User persistUser(User u) {
        UserEntity entity = entityManager.persist(userConverter.convertToEntity(u));
        entityManager.flush();
        return userConverter.convertToDomain(entity);
    }

    @Test
    void saveUser_success() {
        User saved = userRepository.save(user);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test User");
        assertThat(saved.getEmail()).isEqualTo("test@mail.com");
    }

    @Test
    void findById_success() {
        User persisted = persistUser(user);
        Optional<User> found = userRepository.findById(persisted.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(persisted.getId());
        assertThat(found.get().getEmail()).isEqualTo("test@mail.com");
    }

    @Test
    void findByEmail_success() {
        User persisted = persistUser(user);
        User found = userRepository.findByEmail(persisted.getEmail());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(persisted.getId());
    }

    @Test
    void findByEmail_entityIsNull() {
        User found = userRepository.findByEmail(null);
        assertThat(found).isNull();
    }

    @Test
    void findAll_success(){
        for (int i = 0; i < 3; i++) {
            User userToPersist = User.builder()
                    .name("User " + i)
                    .email("test" + i + "@mail.com")
                    .password("password")
                    .isAdmin(false)
                    .build();
            persistUser(userToPersist);
        }
        List<User> found = userRepository.findAll();
        assertThat(found).hasSize(3);
    }

    @Test
    void existsById_success() {
        User persisted = persistUser(user);
        boolean isFound = userRepository.existsById(persisted.getId());
        assertThat(isFound).isTrue();
    }

    @Test
    void existsByEmail_true() {
        persistUser(user);
        assertThat(userRepository.existsByEmail("test@mail.com")).isTrue();
    }

    @Test
    void existsByEmail_false() {
        assertThat(userRepository.existsByEmail("unknown@mail.com")).isFalse();
    }

    @Test
    void deleteById_success() {
        User persisted = persistUser(user);
        userRepository.deleteById(persisted.getId());
        entityManager.flush();
        assertThat(userRepository.findById(persisted.getId())).isEmpty();
    }
}