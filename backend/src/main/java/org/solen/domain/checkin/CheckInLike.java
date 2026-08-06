package org.solen.domain.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CheckInLike {
    @EqualsAndHashCode.Include
    private Long id;
    private Long checkInId;
    private Long userId;
    private LocalDateTime createdAt;
}
