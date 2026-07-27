package org.solen.business.checkin;

import org.solen.domain.checkin.CheckIn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CheckInTimelineBuilder {

    // Post-fetch safety net: after loading check-ins from the DB, re-inspect
    // consecutive entries for the same practice. If the gap in days exceeds the
    // practice's thresholdDays, the streak is reset to 1 (a new chain has begun).
    //
    // The primary streak value is snapshot on the CheckIn at creation time by
    // CreateCheckInUseCaseImpl / UpdateStreakUseCaseImpl. This builder only
    // corrects the display when gaps were missed — e.g. a manual check-in was
    // created without calling updateStreak (which would have reset the streak).
    //
    // No gap-fill entries are inserted for missing days; only actual check-in
    // records appear in the returned timeline.
    public List<CheckIn> buildTimeline(List<CheckIn> rawCheckIns) {
        Map<Long, List<CheckIn>> groupedByPractice = rawCheckIns.stream()
                .collect(Collectors.groupingBy(ci -> ci.getPractice().getId()));

        List<CheckIn> result = new ArrayList<>();

        for (Map.Entry<Long, List<CheckIn>> entry : groupedByPractice.entrySet()) {
            List<CheckIn> sorted = entry.getValue().stream()
                    .sorted(Comparator.comparing((CheckIn ci) -> Objects.requireNonNull(ci.getDate())))
                    .toList();

            int threshold = sorted.get(0).getPractice().getThresholdDays();

            for (CheckIn ci : sorted) {
                if (!result.isEmpty()) {
                    CheckIn last = result.get(result.size() - 1);
                    if (last.getPractice().getId().equals(ci.getPractice().getId())) {
                        long daysBetween = ci.getDate().toEpochDay() - last.getDate().toEpochDay();
                        if (daysBetween > threshold) {
                            ci.setStreakValue(1);
                        }
                    }
                }
                result.add(ci);
            }
        }

        result.sort(Comparator.comparing((CheckIn ci) -> Objects.requireNonNull(ci.getDate())).reversed());
        return result;
    }
}
