package org.solen.business.categorycases;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.controller.dto.category.UpdateCategoryRequest;
import org.solen.domain.practices.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateCategoryUseCaseImpl implements IUpdateCategoryUseCase {

    private final ICategoryRepository categoryRepository;

    public UpdateCategoryUseCaseImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new CategoryNotFoundByIdException(id);
        }

        if (request.getName() != null) {
            category.setName(request.getName());
        }

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId());
            category.setParent(parent);
        } else if (request.isParentExplicitlyNull()) {
            category.setParent(null);
        }

        return categoryRepository.save(category);
    }
}
