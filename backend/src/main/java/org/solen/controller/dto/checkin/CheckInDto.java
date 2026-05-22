package org.solen.controller.dto.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solen.controller.dto.practice.PracticeDto;
import org.solen.domain.checkin.Mood;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckInDto {
    private Long id;
    private PracticeDto practice;
    private LocalDate date;
    private int streakValue;
    private String content;
    private boolean isPublic;
    private Mood mood;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean isLikedByCurrentUser;
}
