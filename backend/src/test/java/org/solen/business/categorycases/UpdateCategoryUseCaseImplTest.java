package org.solen.business.categorycases;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.controller.dto.category.UpdateCategoryRequest;
import org.solen.domain.practices.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseImplTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private UpdateCategoryUseCaseImpl updateCategoryUseCase;

    @Test
    void updateCategoryName() {
        Category existing = Category.builder().id(1L).name("Old Name").build();
        UpdateCategoryRequest request = UpdateCategoryRequest.builder().name("New Name").build();

        when(categoryRepository.findById(1L)).thenReturn(existing);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = updateCategoryUseCase.updateCategory(1L, request);

        assertEquals("New Name", result.getName());
    }

    @Test
    void updateCategory_notFound() {
        when(categoryRepository.findById(99L)).thenReturn(null);
        UpdateCategoryRequest request = UpdateCategoryRequest.builder().build();

        assertThrows(CategoryNotFoundByIdException.class,
                () -> updateCategoryUseCase.updateCategory(99L, request));
    }

    @Test
    void updateCategory_setParentNull() {
        Category existing = Category.builder().id(1L).name("Test").parent(Category.builder().id(2L).build()).build();

        when(categoryRepository.findById(1L)).thenReturn(existing);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = updateCategoryUseCase.updateCategory(1L, UpdateCategoryRequest.withParentNull());

        assertNull(result.getParent());
    }

    @Test
    void updateCategory_setParent_withValidParentId() {
        Category existing = Category.builder().id(1L).name("Child").build();
        Category parent = Category.builder().id(2L).name("Parent").build();
        UpdateCategoryRequest request = UpdateCategoryRequest.builder().parentId(2L).build();

        when(categoryRepository.findById(1L)).thenReturn(existing);
        when(categoryRepository.findById(2L)).thenReturn(parent);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = updateCategoryUseCase.updateCategory(1L, request);

        assertNotNull(result.getParent());
        assertEquals(2L, result.getParent().getId());
    }
}
