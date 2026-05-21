package org.solen.business.categorycases;

import org.solen.business.repos.ICategoryRepository;
import org.solen.domain.practices.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCategoryTreeUseCaseImpl implements IGetCategoryTreeUseCase {

    private final ICategoryRepository categoryRepository;

    public GetCategoryTreeUseCaseImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategoryTree() {
        return categoryRepository.findRootCategories();
    }
}
