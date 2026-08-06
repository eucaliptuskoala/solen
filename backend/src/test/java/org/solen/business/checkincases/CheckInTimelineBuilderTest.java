package org.solen.business.checkincases;

import org.solen.domain.checkin.CheckIn;
import org.solen.domain.practices.Practice;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckInTimelineBuilderTest {

    private final CheckInTimelineBuilder builder = new CheckInTimelineBuilder();

    @Test
    void buildTimeline_singleCheckIn() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Test")
                .streak(1)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .build();

        CheckIn checkIn = CheckIn.builder()
                .id(1L)
                .practice(practice)
                .date(LocalDate.of(2026, 1, 1))
                .streakValue(1)
                .content("Test")
                .isPublic(true)
                .createdAt(LocalDateTime.now())
                .build();

        List<CheckIn> result = builder.buildTimeline(List.of(checkIn));

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStreakValue());
    }

    @Test
    void buildTimeline_noStreakReset() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Test")
                .streak(2)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(3)
                .build();

        CheckIn ci1 = CheckIn.builder().id(1L).practice(practice).date(LocalDate.of(2026, 1, 1)).streakValue(1).build();
        CheckIn ci2 = CheckIn.builder().id(2L).practice(practice).date(LocalDate.of(2026, 1, 3)).streakValue(2).build();

        List<CheckIn> result = builder.buildTimeline(List.of(ci1, ci2));

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getStreakValue());
    }

    @Test
    void buildTimeline_streakResetWhenGapExceedsThreshold() {
        Practice practice = Practice.builder()
                .id(1L)
                .name("Test")
                .streak(2)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(1)
                .build();

        CheckIn ci1 = CheckIn.builder().id(1L).practice(practice).date(LocalDate.of(2026, 1, 1)).streakValue(1).build();
        CheckIn ci2 = CheckIn.builder().id(2L).practice(practice).date(LocalDate.of(2026, 1, 10)).streakValue(2).build();

        List<CheckIn> result = builder.buildTimeline(List.of(ci1, ci2));

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getStreakValue());
    }

    @Test
    void buildTimeline_multiplePractices() {
        Practice p1 = Practice.builder().id(1L).name("P1").thresholdDays(10).build();
        Practice p2 = Practice.builder().id(2L).name("P2").thresholdDays(3).build();

        CheckIn a1 = CheckIn.builder().id(1L).practice(p1).date(LocalDate.of(2026, 1, 1)).streakValue(1).build();
        CheckIn b1 = CheckIn.builder().id(2L).practice(p2).date(LocalDate.of(2026, 1, 2)).streakValue(1).build();
        CheckIn a2 = CheckIn.builder().id(3L).practice(p1).date(LocalDate.of(2026, 1, 5)).streakValue(2).build();

        List<CheckIn> result = builder.buildTimeline(List.of(a1, b1, a2));

        assertEquals(3, result.size());
        assertEquals(2, result.get(0).getStreakValue());
    }
}
