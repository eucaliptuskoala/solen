package org.solen.persistence.repositories;

import org.solen.domain.practices.Category;
import org.solen.persistence.converters.CategoryConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CategoryRepository.class, CategoryConverter.class})
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindCategory() {
        Category category = Category.builder()
                .name("Fitness")
                .build();

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Fitness");

        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Fitness");
    }

    @Test
    void saveCategoryWithParent() {
        Category parent = categoryRepository.save(Category.builder().name("Health").build());
        Category child = Category.builder().name("Workout").parent(parent).build();

        Category saved = categoryRepository.save(child);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getParent()).isNotNull();
        assertThat(saved.getParent().getId()).isEqualTo(parent.getId());
    }

    @Test
    void findRootCategories() {
        categoryRepository.save(Category.builder().name("Root1").build());
        categoryRepository.save(Category.builder().name("Root2").build());
        Category parent = categoryRepository.save(Category.builder().name("Parent").build());
        categoryRepository.save(Category.builder().name("Child").parent(parent).build());

        var roots = categoryRepository.findRootCategories();

        assertThat(roots).hasSize(3).allMatch(c -> c.getParent() == null);
    }

    @Test
    void deleteCategory() {
        Category saved = categoryRepository.save(Category.builder().name("ToDelete").build());

        categoryRepository.deleteById(saved.getId());

        assertThat(categoryRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void findAll_returnsAllCategories() {
        categoryRepository.save(Category.builder().name("A").build());
        categoryRepository.save(Category.builder().name("B").build());

        var all = categoryRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void existsByName_returnsTrue_whenNameExists() {
        categoryRepository.save(Category.builder().name("Unique Name").build());

        boolean exists = categoryRepository.existsByName("Unique Name");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_returnsFalse_whenNameDoesNotExist() {
        boolean exists = categoryRepository.existsByName("Non Existent");

        assertThat(exists).isFalse();
    }
}
