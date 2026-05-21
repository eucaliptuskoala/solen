package org.solen.business.categorycases;

import org.solen.business.repos.ICategoryRepository;
import org.solen.controller.dto.category.CreateCategoryRequest;
import org.solen.domain.practices.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseImplTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryUseCaseImpl createCategoryUseCase;

    @Test
    void createCategory_withoutParent() {
        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("Fitness")
                .build();

        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> {
            Category c = i.getArgument(0);
            c.setId(1L);
            return c;
        });

        Category result = createCategoryUseCase.createCategory(request);

        assertNotNull(result);
        assertEquals("Fitness", result.getName());
        assertNull(result.getParent());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_withParent() {
        Category parent = Category.builder().id(1L).name("Health").build();
        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("Workout")
                .parentId(1L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(parent);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = createCategoryUseCase.createCategory(request);

        assertNotNull(result);
        assertEquals("Workout", result.getName());
        assertNotNull(result.getParent());
        assertEquals(1L, result.getParent().getId());
    }
}
