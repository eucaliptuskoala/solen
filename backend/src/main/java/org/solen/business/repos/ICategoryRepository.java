package org.solen.business.repos;

import org.solen.domain.practices.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    List<Category> findRootCategories();
    void deleteById(Long id);
    boolean existsByName(String name);
}
