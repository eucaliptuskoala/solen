package org.solen.controller.dto.practice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePracticeRequest {
    private Long categoryId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
