package org.solen.domain.practices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solen.domain.users.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Practice {
    private Long id;
    private String name;
    private String description;
    private int streak;
    private LocalDateTime lastUpdatedStreak;
    private int thresholdDays;
    private Category category;
    private User creator;
}