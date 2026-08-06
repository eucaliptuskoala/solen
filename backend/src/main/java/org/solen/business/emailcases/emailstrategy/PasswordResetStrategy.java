package org.solen.business.emailcases.emailstrategy;

import org.solen.domain.email.EmailType;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PasswordResetStrategy implements IEmailStrategy{

    private final Resend resend;

    @Override
    public void send(String to, String url) {
        CreateEmailOptions email = CreateEmailOptions.builder()
            .from("Solen <onboarding@resend.dev>")
            .to(to)
            .subject("Click the link below to reset your password")
            .html("<p>Click <a href='" + url + "'>here</a> to reset your password</p>")
            .build();
        try{        
            resend.emails().send(email);
        }
        catch(ResendException e){
            throw new RuntimeException("Resend has failed");
        }
    }

    @Override
    public EmailType getType() {
        return EmailType.PASSWORD_RESET;
    }    
}
