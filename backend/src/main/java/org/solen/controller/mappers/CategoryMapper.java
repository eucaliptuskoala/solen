package org.solen.controller.mappers;

import org.solen.controller.dto.category.CategoryResponse;
import org.solen.domain.practices.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse convertToResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .children(category.getChildren() != null
                        ? category.getChildren().stream().map(this::convertToResponse).toList()
                        : null)
                .build();
    }
}
