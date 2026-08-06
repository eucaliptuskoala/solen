package org.solen.business.checkin.fypstrategy;

import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IPracticeRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticeBasedRecommendationTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @Mock
    private IPracticeRepository practiceRepository;

    @InjectMocks
    private PracticeBasedRecommendation recommendation;

    private User makeUser(Long id) {
        return User.builder().id(id).build();
    }

    private Practice makePractice(Long id, Category category, User creator) {
        return Practice.builder().id(id).category(category).creator(creator).build();
    }

    private CheckIn makePublicCheckIn(Long id, Practice practice) {
        return CheckIn.builder().id(id).practice(practice).build();
    }

    @Test
    void findPublicCheckIns_filtersByUserCategories() {
        Category fitness = Category.builder().id(1L).name("Fitness").build();
        User user = makeUser(1L);
        User other = makeUser(2L);

        Practice userPractice = makePractice(1L, fitness, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        Practice fitnessPractice = makePractice(10L, fitness, other);
        CheckIn matched = makePublicCheckIn(1L, fitnessPractice);
        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of(matched));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }

    @Test
    void findPublicCheckIns_usesUserIdForOwnExclusion() {
        Category fitness = Category.builder().id(1L).name("Fitness").build();
        User user = makeUser(1L);

        Practice userPractice = makePractice(1L, fitness, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        Practice ownPractice = makePractice(10L, fitness, user);
        CheckIn ownCheckIn = makePublicCheckIn(1L, ownPractice);
        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of(ownCheckIn));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }

    @Test
    void findPublicCheckIns_deduplicatesCategoryIds() {
        Category fitness = Category.builder().id(1L).name("Fitness").build();
        User user = makeUser(1L);

        Practice p1 = makePractice(1L, fitness, user);
        Practice p2 = makePractice(2L, fitness, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(p1, p2));

        CheckIn any = makePublicCheckIn(1L, makePractice(10L, fitness, makeUser(2L)));
        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of(any));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }

    @Test
    void findPublicCheckIns_noCategories_returnsEmpty() {
        User user = makeUser(1L);

        Practice userPractice = makePractice(1L, null, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        when(checkInRepository.findPublicCheckInsForCategories(List.of(), 1L)).thenReturn(List.of());

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(), 1L);
    }

    @Test
    void findPublicCheckIns_noMatches_returnsEmpty() {
        User user = makeUser(1L);
        User other = makeUser(2L);

        Practice userPractice = makePractice(1L, Category.builder().id(1L).name("Fitness").build(), user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        when(checkInRepository.findPublicCheckInsForCategories(List.of(1L), 1L)).thenReturn(List.of());

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
        verify(checkInRepository).findPublicCheckInsForCategories(List.of(1L), 1L);
    }
}
