package org.solen.controller.mappers;

import org.solen.controller.dto.category.CategoryResponse;
import org.solen.domain.habits.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @InjectMocks
    private CategoryMapper mapper;

    @Test
    void convertToResponse_null_returnsNull() {
        assertNull(mapper.convertToResponse(null));
    }

    @Test
    void convertToResponse_mapsAllFields() {
        Category category = Category.builder()
                .id(1L)
                .name("Fitness")
                .children(List.of(
                        Category.builder().id(2L).name("Running").build()
                ))
                .build();

        CategoryResponse response = mapper.convertToResponse(category);

        assertEquals(1L, response.getId());
        assertEquals("Fitness", response.getName());
        assertEquals(1, response.getChildren().size());
        assertEquals(2L, response.getChildren().get(0).getId());
        assertEquals("Running", response.getChildren().get(0).getName());
    }

    @Test
    void convertToResponse_nullChildren_returnsNullChildren() {
        Category category = Category.builder()
                .id(1L)
                .name("Fitness")
                .children(null)
                .build();

        CategoryResponse response = mapper.convertToResponse(category);

        assertNull(response.getChildren());
    }
}
