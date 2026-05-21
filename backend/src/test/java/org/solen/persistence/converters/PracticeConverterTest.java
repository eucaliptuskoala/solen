package org.solen.persistence.converters;

import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.solen.persistence.entities.CategoryEntity;
import org.solen.persistence.entities.PracticeEntity;
import org.solen.persistence.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticeConverterTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private CategoryConverter categoryConverter;

    @InjectMocks
    private PracticeConverter converter;

    @Test
    void convertToEntity_mapsAllFields() {
        User user = User.builder().id(1L).build();
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        Category category = Category.builder().id(1L).name("Fitness").build();
        CategoryEntity categoryEntity = CategoryEntity.builder().id(1L).name("Fitness").build();
        when(userConverter.convertToEntity(user)).thenReturn(userEntity);
        when(categoryConverter.convertToEntity(category)).thenReturn(categoryEntity);

        Practice practice = Practice.builder()
                .id(1L).name("Run").description("Run daily")
                .streak(5).lastUpdatedStreak(LocalDateTime.of(2026, 5, 21, 10, 0))
                .thresholdDays(2).category(category).creator(user)
                .build();

        PracticeEntity entity = converter.convertToEntity(practice);

        assertEquals(1L, entity.getId());
        assertEquals("Run", entity.getName());
        assertEquals(5, entity.getStreak());
        assertEquals(2, entity.getThresholdDays());
        assertNotNull(entity.getCreator());
        assertNotNull(entity.getCategory());
        verify(userConverter).convertToEntity(user);
        verify(categoryConverter).convertToEntity(category);
    }

    @Test
    void convertToDomain_mapsAllFields() {
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        CategoryEntity categoryEntity = CategoryEntity.builder().id(1L).name("Fitness").build();
        Category category = Category.builder().id(1L).name("Fitness").build();
        when(userConverter.convertToDomain(userEntity)).thenReturn(user);
        when(categoryConverter.convertToDomain(categoryEntity)).thenReturn(category);

        PracticeEntity entity = PracticeEntity.builder()
                .id(1L).name("Run").description("Run daily")
                .streak(5).lastUpdatedStreak(LocalDateTime.of(2026, 5, 21, 10, 0))
                .thresholdDays(2).category(categoryEntity).creator(userEntity)
                .build();

        Practice practice = converter.convertToDomain(entity);

        assertEquals(1L, practice.getId());
        assertEquals("Run", practice.getName());
        assertEquals(5, practice.getStreak());
        assertEquals(2, practice.getThresholdDays());
        assertNotNull(practice.getCreator());
        assertNotNull(practice.getCategory());
        verify(userConverter).convertToDomain(userEntity);
        verify(categoryConverter).convertToDomain(categoryEntity);
    }

    @Test
    void convertToEntity_nullCategory() {
        User user = User.builder().id(1L).build();
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        when(userConverter.convertToEntity(user)).thenReturn(userEntity);
        when(categoryConverter.convertToEntity(null)).thenReturn(null);

        Practice practice = Practice.builder()
                .id(1L).name("Read").streak(0).thresholdDays(1)
                .category(null).creator(user)
                .build();

        PracticeEntity entity = converter.convertToEntity(practice);

        assertNull(entity.getCategory());
    }
}
