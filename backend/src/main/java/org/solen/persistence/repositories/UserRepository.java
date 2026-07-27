package org.solen.persistence.repositories;

import lombok.AllArgsConstructor;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.solen.persistence.converters.UserConverter;
import org.solen.persistence.entities.UserEntity;
import org.solen.persistence.jparepos.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class UserRepository  implements IUserRepository {

    private UserJpaRepository jpaRepository;
    private UserConverter converter;

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return jpaRepository.existsById(id);
    }

    @Override
    public User save(User user) {
        UserEntity entity = converter.convertToEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return converter.convertToDomain(savedEntity);
    }

    @Override
    public User findById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        UserEntity entity = jpaRepository.findById(id).orElse(null);
        if (entity == null) return null;
        return converter.convertToDomain(entity);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity entity = jpaRepository.findByEmail(email);
        if (entity == null) return null;
        return converter.convertToDomain(entity);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(converter::convertToDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        jpaRepository.deleteById(id);
    }
}