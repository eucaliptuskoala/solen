package org.solen.business.categorycases;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.domain.practices.Category;
import org.springframework.stereotype.Service;

@Service
public class GetCategoryByIdUseCaseImpl implements IGetCategoryByIdUseCase {

    private final ICategoryRepository categoryRepository;

    public GetCategoryByIdUseCaseImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new CategoryNotFoundByIdException(id);
        }
        return category;
    }
}
