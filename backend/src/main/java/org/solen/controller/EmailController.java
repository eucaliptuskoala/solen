package org.solen.controller;

import org.solen.business.emailusecases.ISendEmailUseCase;
import org.solen.configuration.security.UserInfoProvider;
import org.solen.controller.dto.email.SendEmailRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/emails")
@AllArgsConstructor
public class EmailController {

    private final ISendEmailUseCase sendEmailUseCase;
    private final UserInfoProvider userInfoProvider;

    @PostMapping
    public void send(@Valid @RequestBody SendEmailRequest request){
        String targetEmail = request.getEmail() != null ? request.getEmail() : userInfoProvider.getUserEmail();
        sendEmailUseCase.execute(request.getEmailType(), targetEmail);
    }
}
