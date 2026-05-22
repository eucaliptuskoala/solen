package org.solen.controller.dto.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToggleLikeResponse {
    private boolean liked;
    private int likeCount;
}
