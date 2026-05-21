package org.solen.domain.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solen.domain.practices.Practice;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckIn {
    private Long id;
    private Practice practice;
    private LocalDate date;
    private int streakValue;
    private String content;
    private boolean isPublic;
    private Mood mood;
    private LocalDateTime createdAt;
}
