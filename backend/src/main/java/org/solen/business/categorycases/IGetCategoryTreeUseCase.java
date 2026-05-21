package org.solen.business.categorycases;

import org.solen.domain.practices.Category;

import java.util.List;

public interface IGetCategoryTreeUseCase {
    List<Category> getCategoryTree();
}
