package org.solen.controller.dto.checkin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solen.domain.checkin.Mood;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCheckInRequest {
    @NotNull
    private Long practiceId;
    private String content;
    private boolean isPublic;
    private Mood mood;
}
