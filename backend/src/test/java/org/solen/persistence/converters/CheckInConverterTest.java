package org.solen.persistence.converters;

import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.practices.Practice;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.entities.PracticeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInConverterTest {

    @Mock
    private PracticeConverter practiceConverter;

    @InjectMocks
    private CheckInConverter converter;

    @Test
    void convertToEntity_mapsAllFields() {
        Practice practice = Practice.builder().id(1L).build();
        PracticeEntity practiceEntity = PracticeEntity.builder().id(1L).build();
        when(practiceConverter.convertToEntity(practice)).thenReturn(practiceEntity);

        CheckIn checkIn = CheckIn.builder()
                .id(1L).practice(practice)
                .date(LocalDate.of(2026, 5, 21))
                .streakValue(3).content("Good").isPublic(true)
                .mood(Mood.GOOD).createdAt(LocalDateTime.of(2026, 5, 21, 10, 0))
                .build();

        CheckInEntity entity = converter.convertToEntity(checkIn);

        assertEquals(1L, entity.getId());
        assertEquals(LocalDate.of(2026, 5, 21), entity.getDate());
        assertEquals(3, entity.getStreakValue());
        assertTrue(entity.isPublic());
        assertEquals("GOOD", entity.getMood());
        assertNotNull(entity.getPractice());
        verify(practiceConverter).convertToEntity(practice);
    }

    @Test
    void convertToDomain_mapsAllFields() {
        PracticeEntity practiceEntity = PracticeEntity.builder().id(1L).build();
        Practice practice = Practice.builder().id(1L).build();
        when(practiceConverter.convertToDomain(practiceEntity)).thenReturn(practice);

        CheckInEntity entity = CheckInEntity.builder()
                .id(1L).practice(practiceEntity)
                .date(LocalDate.of(2026, 5, 21))
                .streakValue(3).content("Good").isPublic(true)
                .mood("GOOD").createdAt(LocalDateTime.of(2026, 5, 21, 10, 0))
                .build();

        CheckIn checkIn = converter.convertToDomain(entity);

        assertEquals(1L, checkIn.getId());
        assertEquals(LocalDate.of(2026, 5, 21), checkIn.getDate());
        assertEquals(3, checkIn.getStreakValue());
        assertEquals("Good", checkIn.getContent());
        assertTrue(checkIn.isPublic());
        assertEquals(Mood.GOOD, checkIn.getMood());
        assertNotNull(checkIn.getPractice());
        verify(practiceConverter).convertToDomain(practiceEntity);
    }
}
