package org.solen.domain.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckInLike {
    private Long id;
    private Long checkInId;
    private Long userId;
    private LocalDateTime createdAt;
}
