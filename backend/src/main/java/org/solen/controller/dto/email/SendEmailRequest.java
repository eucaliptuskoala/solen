package org.solen.controller.dto.email;

import org.solen.domain.email.EmailType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    private EmailType emailType;
    private String email;
}
