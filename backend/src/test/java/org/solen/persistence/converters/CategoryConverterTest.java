package org.solen.persistence.converters;

import org.solen.domain.habits.Category;
import org.solen.persistence.entities.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryConverterTest {

    @InjectMocks
    private CategoryConverter converter;

    @Test
    void convertToEntity_null_returnsNull() {
        assertNull(converter.convertToEntity(null));
    }

    @Test
    void convertToDomain_null_returnsNull() {
        assertNull(converter.convertToDomain(null));
    }

    @Test
    void convertToDomainWithChildren_null_returnsNull() {
        assertNull(converter.convertToDomainWithChildren(null));
    }

    @Test
    void convertToEntity_mapsAllFields() {
        Category domain = Category.builder()
                .id(1L).name("Fitness")
                .parent(Category.builder().id(2L).name("Health").build())
                .build();

        CategoryEntity entity = converter.convertToEntity(domain);

        assertEquals(1L, entity.getId());
        assertEquals("Fitness", entity.getName());
        assertNotNull(entity.getParent());
        assertEquals(2L, entity.getParent().getId());
    }

    @Test
    void convertToDomain_mapsAllFields() {
        CategoryEntity entity = CategoryEntity.builder()
                .id(1L).name("Fitness")
                .parent(CategoryEntity.builder().id(2L).name("Health").build())
                .build();

        Category domain = converter.convertToDomain(entity);

        assertEquals(1L, domain.getId());
        assertEquals("Fitness", domain.getName());
        assertNotNull(domain.getParent());
        assertEquals(2L, domain.getParent().getId());
    }

    @Test
    void convertToDomainWithChildren_mapsChildren() {
        CategoryEntity child = CategoryEntity.builder()
                .id(2L).name("Running")
                .parent(null)
                .build();
        CategoryEntity entity = CategoryEntity.builder()
                .id(1L).name("Fitness")
                .parent(null)
                .children(java.util.List.of(child))
                .build();

        Category domain = converter.convertToDomainWithChildren(entity);

        assertEquals(1L, domain.getId());
        assertNotNull(domain.getChildren());
        assertEquals(1, domain.getChildren().size());
        assertEquals(2L, domain.getChildren().get(0).getId());
    }

    @Test
    void convertToEntity_roundTrip() {
        Category domain = Category.builder().id(1L).name("Fitness").build();

        CategoryEntity entity = converter.convertToEntity(domain);
        Category back = converter.convertToDomain(entity);

        assertEquals(domain.getId(), back.getId());
        assertEquals(domain.getName(), back.getName());
    }
}
