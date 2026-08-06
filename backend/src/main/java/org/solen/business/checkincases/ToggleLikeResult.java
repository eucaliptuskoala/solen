package org.solen.business.checkincases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToggleLikeResult {
    private boolean liked;
    private int likeCount;
}
