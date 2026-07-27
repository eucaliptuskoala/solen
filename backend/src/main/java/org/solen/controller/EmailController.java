package org.solen.controller;

import org.solen.business.emailusecases.ISendEmailUseCaseImpl;
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

    private final ISendEmailUseCaseImpl sendEmailUseCase;
    private final UserInfoProvider userInfoProvider;

    @PostMapping
    public void send(@Valid @RequestBody SendEmailRequest request){
        if(request.getEmail() == null){
            String authEmail = userInfoProvider.getUserEmail();
            sendEmailUseCase.execute(request.getEmailType(), authEmail);
        }
        else{
            sendEmailUseCase.execute(request.getEmailType(), request.getEmail());
        }
    }
}
