package org.solen.business.categorycases;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.domain.practices.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseImplTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private GetCategoryByIdUseCaseImpl getCategoryByIdUseCase;

    @Test
    void getCategoryById_success() {
        Category category = Category.builder().id(1L).name("Fitness").build();
        when(categoryRepository.findById(1L)).thenReturn(category);

        Category result = getCategoryByIdUseCase.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Fitness", result.getName());
    }

    @Test
    void getCategoryById_notFound() {
        when(categoryRepository.findById(99L)).thenReturn(null);

        assertThrows(CategoryNotFoundByIdException.class, () -> getCategoryByIdUseCase.getCategoryById(99L));
    }
}
