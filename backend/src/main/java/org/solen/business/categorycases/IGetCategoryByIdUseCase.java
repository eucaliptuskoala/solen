package org.solen.business.categorycases;

import org.solen.domain.practices.Category;

public interface IGetCategoryByIdUseCase {
    Category getCategoryById(Long id);
}
