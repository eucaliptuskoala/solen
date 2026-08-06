package org.solen.domain.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.solen.domain.practices.Practice;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CheckIn {
    @EqualsAndHashCode.Include
    private Long id;
    private Practice practice;
    private LocalDate date;
    private int streakValue;
    private String content;
    private boolean isPublic;
    private Mood mood;
    private LocalDateTime createdAt;
}
