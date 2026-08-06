package org.solen.domain.practices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.solen.domain.users.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Practice {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private int streak;
    private LocalDateTime lastUpdatedStreak;
    private int thresholdDays;
    private Category category;
    private User creator;
}