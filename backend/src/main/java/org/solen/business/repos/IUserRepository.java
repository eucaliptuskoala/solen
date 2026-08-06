package org.solen.business.repos;

import org.solen.domain.users.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    User save(User user);
    Optional<User> findById(Long id);
    User findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);
}