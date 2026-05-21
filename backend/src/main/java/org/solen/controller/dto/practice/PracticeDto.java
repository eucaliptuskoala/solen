package org.solen.controller.dto.practice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PracticeDto {
    private Long id;
    private String name;
    private String description;
    private int streak;
    private LocalDateTime lastUpdatedStreak;
    private int thresholdDays;
    private Long categoryId;
    private String categoryName;
    private boolean checkedInToday;
}