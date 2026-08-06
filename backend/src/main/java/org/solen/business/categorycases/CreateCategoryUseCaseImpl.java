package org.solen.business.categorycases;

import org.solen.controller.dto.category.CreateCategoryRequest;
import org.solen.domain.practices.Category;
import org.solen.business.repos.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCategoryUseCaseImpl implements ICreateCategoryUseCase {

    private final ICategoryRepository categoryRepository;

    public CreateCategoryUseCaseImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(CreateCategoryRequest request) {
        Category parent = request.getParentId() != null
                ? categoryRepository.findById(request.getParentId()).orElse(null)
                : null;

        return categoryRepository.save(Category.builder()
                .name(request.getName())
                .parent(parent)
                .build());
    }
}
