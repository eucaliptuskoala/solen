package org.solen.business.checkin.fypstrategy;

import org.solen.business.repos.ICheckInRepository;
import org.solen.business.repos.IHabitRepository;
import org.solen.domain.checkin.CheckIn;
import org.solen.domain.habits.Category;
import org.solen.domain.habits.Habit;
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
class HabitBasedRecommendationTest {

    @Mock
    private ICheckInRepository checkInRepository;

    @Mock
    private IHabitRepository habitRepository;

    @InjectMocks
    private HabitBasedRecommendation recommendation;

    private User makeUser(Long id) {
        return User.builder().id(id).build();
    }

    private Habit makeHabit(Long id, Category category, User creator) {
        return Habit.builder().id(id).category(category).creator(creator).build();
    }

    private CheckIn makePublicCheckIn(Long id, Habit habit) {
        return CheckIn.builder().id(id).habit(habit).build();
    }

    @Test
    void findPublicCheckIns_filtersByUserCategories() {
        Category fitness = Category.builder().id(1L).name("Fitness").build();
        Category reading = Category.builder().id(2L).name("Reading").build();
        User user = makeUser(1L);
        User other = makeUser(2L);

        Habit userHabit = makeHabit(1L, fitness, user);
        when(habitRepository.findByCreatorId(1L)).thenReturn(List.of(userHabit));

        Habit fitnessHabit = makeHabit(10L, fitness, other);
        Habit readingHabit = makeHabit(11L, reading, other);
        CheckIn matched = makePublicCheckIn(1L, fitnessHabit);
        CheckIn unmatched = makePublicCheckIn(2L, readingHabit);
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(matched, unmatched));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void findPublicCheckIns_excludesOwnCheckIns() {
        Category fitness = Category.builder().id(1L).name("Fitness").build();
        User user = makeUser(1L);

        Habit userHabit = makeHabit(1L, fitness, user);
        when(habitRepository.findByCreatorId(1L)).thenReturn(List.of(userHabit));

        Habit ownHabit = makeHabit(10L, fitness, user);
        CheckIn ownCheckIn = makePublicCheckIn(1L, ownHabit);
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(ownCheckIn));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicCheckIns_handlesNullCategory() {
        User user = makeUser(1L);
        User other = makeUser(2L);

        Habit userHabit = makeHabit(1L, null, user);
        when(habitRepository.findByCreatorId(1L)).thenReturn(List.of(userHabit));

        CheckIn any = makePublicCheckIn(1L, makeHabit(10L, Category.builder().id(1L).build(), other));
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(any));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicCheckIns_noMatches_returnsEmpty() {
        User user = makeUser(1L);
        User other = makeUser(2L);

        Habit userHabit = makeHabit(1L, Category.builder().id(1L).name("Fitness").build(), user);
        when(habitRepository.findByCreatorId(1L)).thenReturn(List.of(userHabit));

        CheckIn wrongCategory = makePublicCheckIn(1L,
                makeHabit(10L, Category.builder().id(2L).name("Reading").build(), other));
        when(checkInRepository.findPublicCheckIns()).thenReturn(List.of(wrongCategory));

        List<CheckIn> result = recommendation.findPublicCheckIns(1L);

        assertTrue(result.isEmpty());
    }
}
