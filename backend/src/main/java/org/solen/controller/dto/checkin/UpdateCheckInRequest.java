package org.solen.controller.dto.checkin;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solen.domain.checkin.Mood;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCheckInRequest {
    @Size(max = 1000)
    private String content;
    private boolean isPublic;
    private Mood mood;
}
