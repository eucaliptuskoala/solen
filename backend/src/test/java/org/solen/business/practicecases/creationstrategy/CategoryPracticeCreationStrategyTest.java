package org.solen.business.practicecases.creationstrategy;

import org.solen.business.exceptions.CategoryNotFoundByIdException;
import org.solen.business.exceptions.PracticeAlreadyExistsException;
import org.solen.business.exceptions.UserNotFoundByIdException;
import org.solen.business.repos.ICategoryRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.junit.jupiter.api.BeforeEach;

import static org.solen.business.practicecases.creationstrategy.NameUtils.normalizeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoryPracticeCreationStrategyTest {

    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private IPracticeRepository practiceRepository;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CategoryPracticeCreationStrategy strategy;

    private User user;
    private Category category;
    private Long categoryId;
    private String name;
    private String description;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("User")
                .email("user@test.com")
                .password("password")
                .isAdmin(false)
                .build();

        category = Category.builder()
                .id(1L)
                .name("Fitness")
                .build();

        categoryId = 1L;
        name = "  morning workout  ";
        description = "Daily morning exercise";
    }

    @Test
    void createPractice_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of());
        when(practiceRepository.save(any(Practice.class))).thenAnswer(i -> i.getArgument(0));

        Practice result = strategy.createPractice(categoryId, name, description, 1L);

        assertNotNull(result);
        assertEquals("Morning workout", result.getName());
        assertEquals("Daily morning exercise", result.getDescription());
        assertEquals(category, result.getCategory());
        assertEquals(user, result.getCreator());
        assertEquals(0, result.getStreak());
        assertEquals(1, result.getThresholdDays());

        verify(categoryRepository).findById(1L);
        verify(practiceRepository).save(any(Practice.class));
    }

    @Test
    void createPractice_categoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundByIdException.class, () -> strategy.createPractice(categoryId, name, description, 1L));
        verify(practiceRepository, never()).save(any());
    }

    @Test
    void createPractice_userNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundByIdException.class, () -> strategy.createPractice(categoryId, name, description, 1L));
        verify(practiceRepository, never()).save(any());
    }

    @Test
    void createPractice_duplicateName() {
        Practice existing = Practice.builder().name("Morning workout").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(existing));

        assertThrows(PracticeAlreadyExistsException.class, () -> strategy.createPractice(categoryId, name, description, 1L));
        verify(practiceRepository, never()).save(any());
    }

    @Test
    void normalizeName_trimsAndCapitalizes() {
        assertEquals("Hello", normalizeName("  hello  "));
        assertEquals("World", normalizeName("WORLD"));
        assertEquals("Test", normalizeName("test"));
    }
}
