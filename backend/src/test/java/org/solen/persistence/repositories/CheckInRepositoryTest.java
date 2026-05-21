package org.solen.persistence.repositories;

import org.solen.domain.checkin.CheckIn;
import org.solen.domain.checkin.Mood;
import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.solen.persistence.converters.CheckInConverter;
import org.solen.persistence.entities.CheckInEntity;
import org.solen.persistence.jparepos.CheckInJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInRepositoryTest {

    @Mock
    private CheckInConverter converter;

    @Mock
    private CheckInJpaRepository jpaRepository;

    @InjectMocks
    private CheckInRepository checkInRepository;

    @Test
    void saveCheckIn() {
        Practice practice = Practice.builder().id(1L).build();
        CheckInEntity entity = new CheckInEntity();
        CheckIn domain = CheckIn.builder().id(1L).practice(practice).date(LocalDate.now()).streakValue(1).build();

        when(converter.convertToEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(converter.convertToDomain(entity)).thenReturn(domain);

        CheckIn result = checkInRepository.save(domain);

        assertNotNull(result);
        assertEquals(1, result.getStreakValue());
        verify(jpaRepository, times(1)).save(entity);
    }

    @Test
    void findPublicCheckIns() {
        CheckInEntity entity = new CheckInEntity();
        Practice practice = Practice.builder()
                .id(1L)
                .category(Category.builder().id(1L).name("Fitness").build())
                .creator(User.builder().id(1L).email("user@test.com").build())
                .build();
        CheckIn publicCheckIn = CheckIn.builder()
                .id(1L).practice(practice).date(LocalDate.now()).streakValue(5)
                .content("Great workout!").isPublic(true).mood(Mood.AWESOME).build();

        when(jpaRepository.findPublicCheckIns()).thenReturn(List.of(entity));
        when(converter.convertToDomain(entity)).thenReturn(publicCheckIn);

        List<CheckIn> result = checkInRepository.findPublicCheckIns();

        assertEquals(1, result.size());
        assertEquals("Great workout!", result.get(0).getContent());
        verify(jpaRepository, times(1)).findPublicCheckIns();
    }

    @Test
    void findByPracticeCreatorId() {
        CheckInEntity entity = new CheckInEntity();
        Practice practice = Practice.builder().id(1L).creator(User.builder().id(1L).build()).build();
        CheckIn checkIn = CheckIn.builder().id(1L).practice(practice).date(LocalDate.now()).streakValue(3).build();

        when(jpaRepository.findByPracticeCreatorId(1L)).thenReturn(List.of(entity));
        when(converter.convertToDomain(entity)).thenReturn(checkIn);

        List<CheckIn> found = checkInRepository.findByPracticeCreatorId(1L);

        assertEquals(1, found.size());
        verify(jpaRepository, times(1)).findByPracticeCreatorId(1L);
    }

    @Test
    void findCheckInsForUser() {
        CheckInEntity entity1 = new CheckInEntity();
        CheckInEntity entity2 = new CheckInEntity();
        Practice practice = Practice.builder().id(1L).creator(User.builder().id(1L).build()).build();
        CheckIn ci1 = CheckIn.builder().id(1L).practice(practice).date(LocalDate.now().minusDays(1)).streakValue(1).build();
        CheckIn ci2 = CheckIn.builder().id(2L).practice(practice).date(LocalDate.now()).streakValue(2).build();

        when(jpaRepository.findCheckInsForUser(1L, LocalDate.now().minusDays(1), LocalDate.now()))
                .thenReturn(List.of(entity1, entity2));
        when(converter.convertToDomain(entity1)).thenReturn(ci1);
        when(converter.convertToDomain(entity2)).thenReturn(ci2);

        List<CheckIn> found = checkInRepository.findCheckInsForUser(1L, LocalDate.now().minusDays(1), LocalDate.now());

        assertEquals(2, found.size());
        verify(jpaRepository, times(1)).findCheckInsForUser(1L, LocalDate.now().minusDays(1), LocalDate.now());
    }
}
