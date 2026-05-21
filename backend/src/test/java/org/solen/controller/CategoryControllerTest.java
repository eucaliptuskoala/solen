package org.solen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.solen.business.categorycases.*;
import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.usercases.UserDetailsService;
import org.solen.configuration.GlobalExceptionHandler;
import org.solen.configuration.security.JwtUtil;
import org.solen.controller.dto.category.CategoryResponse;
import org.solen.controller.dto.category.CreateCategoryRequest;
import org.solen.controller.dto.category.UpdateCategoryRequest;
import org.solen.controller.mappers.CategoryMapper;
import org.solen.domain.practices.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private IGetCategoryTreeUseCase getCategoryTreeUseCase;

    @MockitoBean
    private IGetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private IUpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private IDeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private CategoryMapper categoryMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getCategoryTree_returnsList() throws Exception {
        Category root = Category.builder().id(1L).name("Wellness").build();
        when(getCategoryTreeUseCase.getCategoryTree()).thenReturn(List.of(root));
        when(categoryMapper.convertToResponse(root))
                .thenReturn(CategoryResponse.builder().id(1L).name("Wellness").build());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Wellness"));
    }

    @Test
    void getCategoryById_returnsCategory() throws Exception {
        Category category = Category.builder().id(1L).name("Fitness").build();
        when(getCategoryByIdUseCase.getCategoryById(1L)).thenReturn(category);
        when(categoryMapper.convertToResponse(category))
                .thenReturn(CategoryResponse.builder().id(1L).name("Fitness").build());

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fitness"));
    }

    @Test
    void getCategoryById_notFound_returns404() throws Exception {
        when(getCategoryByIdUseCase.getCategoryById(99L))
                .thenThrow(new CategoryNotFoundByIdException(99L));

        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createCategory_returnsCreatedCategory() throws Exception {
        CreateCategoryRequest request = CreateCategoryRequest.builder().name("Mindfulness").build();
        Category category = Category.builder().id(1L).name("Mindfulness").build();
        when(createCategoryUseCase.createCategory(any(CreateCategoryRequest.class))).thenReturn(category);
        when(categoryMapper.convertToResponse(category))
                .thenReturn(CategoryResponse.builder().id(1L).name("Mindfulness").build());

        mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mindfulness"));
    }

    @Test
    @WithMockUser
    void updateCategory_returnsUpdatedCategory() throws Exception {
        UpdateCategoryRequest request = UpdateCategoryRequest.builder().name("Wellness Updated").build();
        Category category = Category.builder().id(1L).name("Wellness Updated").build();
        when(updateCategoryUseCase.updateCategory(eq(1L), any(UpdateCategoryRequest.class))).thenReturn(category);
        when(categoryMapper.convertToResponse(category))
                .thenReturn(CategoryResponse.builder().id(1L).name("Wellness Updated").build());

        mockMvc.perform(put("/admin/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Wellness Updated"));
    }

    @Test
    @WithMockUser
    void deleteCategory_returns204() throws Exception {
        mockMvc.perform(delete("/admin/categories/1"))
                .andExpect(status().isNoContent());

        verify(deleteCategoryUseCase).deleteCategory(1L);
    }

    @Test
    @WithMockUser
    void deleteCategory_notFound_returns404() throws Exception {
        doThrow(new CategoryNotFoundByIdException(99L))
                .when(deleteCategoryUseCase).deleteCategory(99L);

        mockMvc.perform(delete("/admin/categories/99"))
                .andExpect(status().isNotFound());
    }
}
