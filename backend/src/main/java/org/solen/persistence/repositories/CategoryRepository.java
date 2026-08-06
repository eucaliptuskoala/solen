package org.solen.persistence.repositories;

import lombok.AllArgsConstructor;
import org.solen.business.repos.ICategoryRepository;
import org.solen.domain.practices.Category;
import org.solen.persistence.converters.CategoryConverter;
import org.solen.persistence.entities.CategoryEntity;
import org.solen.persistence.jparepos.CategoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CategoryRepository implements ICategoryRepository {

    private CategoryJpaRepository jpaRepository;
    private CategoryConverter converter;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = jpaRepository.save(converter.convertToEntity(category));
        return converter.convertToDomain(entity);
    }

    @Override
    public Optional<Category> findById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return jpaRepository.findById(id).map(converter::convertToDomainWithChildren);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream().map(converter::convertToDomain).toList();
    }

    @Override
    public List<Category> findRootCategories() {
        return jpaRepository.findByParentIsNull().stream()
                .map(converter::convertToDomainWithChildren)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
