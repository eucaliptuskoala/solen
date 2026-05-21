package org.solen.business.categorycases;

import org.solen.business.repos.ICategoryRepository;
import org.solen.domain.practices.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCategoryTreeUseCaseImplTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private GetCategoryTreeUseCaseImpl getCategoryTreeUseCase;

    @Test
    void getCategoryTree_returnsRootCategories() {
        Category root1 = Category.builder().id(1L).name("Fitness").build();
        Category root2 = Category.builder().id(2L).name("Nutrition").build();

        when(categoryRepository.findRootCategories()).thenReturn(List.of(root1, root2));

        List<Category> result = getCategoryTreeUseCase.getCategoryTree();

        assertEquals(2, result.size());
        assertEquals("Fitness", result.get(0).getName());
        assertEquals("Nutrition", result.get(1).getName());
    }

    @Test
    void getCategoryTree_empty() {
        when(categoryRepository.findRootCategories()).thenReturn(List.of());

        List<Category> result = getCategoryTreeUseCase.getCategoryTree();

        assertTrue(result.isEmpty());
    }
}
