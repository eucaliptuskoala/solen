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
        Category reading = Category.builder().id(2L).name("Reading").build();
        User user = makeUser(1L);
        User other = makeUser(2L);

        Practice userPractice = makePractice(1L, fitness, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        Practice fitnessPractice = makePractice(10L, fitness, other);
        Practice readingPractice = makePractice(11L, reading, other);
        CheckIn matched = makePublicCheckIn(1L, fitnessPractice);
        CheckIn unmatched = makePublicCheckIn(2L, readingPractice);
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(matched, unmatched));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void findPublicCheckIns_excludesOwnCheckIns() {
        Category fitness = Category.builder().id(1L).name("Fitness").build();
        User user = makeUser(1L);

        Practice userPractice = makePractice(1L, fitness, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        Practice ownPractice = makePractice(10L, fitness, user);
        CheckIn ownCheckIn = makePublicCheckIn(1L, ownPractice);
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(ownCheckIn));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicCheckIns_handlesNullCategory() {
        User user = makeUser(1L);
        User other = makeUser(2L);

        Practice userPractice = makePractice(1L, null, user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        CheckIn any = makePublicCheckIn(1L, makePractice(10L, Category.builder().id(1L).build(), other));
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(any));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicCheckIns_noMatches_returnsEmpty() {
        User user = makeUser(1L);
        User other = makeUser(2L);

        Practice userPractice = makePractice(1L, Category.builder().id(1L).name("Fitness").build(), user);
        when(practiceRepository.findByCreatorId(1L)).thenReturn(List.of(userPractice));

        CheckIn wrongCategory = makePublicCheckIn(1L,
                makePractice(10L, Category.builder().id(2L).name("Reading").build(), other));
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(wrongCategory));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
    }
}
