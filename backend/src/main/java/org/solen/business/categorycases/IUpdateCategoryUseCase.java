package org.solen.business.categorycases;

import org.solen.controller.dto.category.UpdateCategoryRequest;
import org.solen.domain.practices.Category;

public interface IUpdateCategoryUseCase {
    Category updateCategory(Long id, UpdateCategoryRequest request);
}
