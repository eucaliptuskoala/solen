package org.solen.business.categorycases;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteCategoryUseCaseImpl implements IDeleteCategoryUseCase {

    private final ICategoryRepository categoryRepository;

    public DeleteCategoryUseCaseImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new CategoryNotFoundByIdException(id);
        }
        categoryRepository.deleteById(id);
    }
}
