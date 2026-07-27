package org.solen.controller.dto.email;

import org.solen.domain.email.EmailType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    @NotNull
    private EmailType emailType;
    private String email;
}
