package org.solen.business.emailusecases.emailstrategy;

import org.solen.domain.email.EmailType;
import org.springframework.stereotype.Component;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

@Component
public class EmailVerificationStrategy implements IEmailStrategy{

    private Resend resend;

    @Override
    public void send(String to, String url){

        CreateEmailOptions email = CreateEmailOptions.builder()
            .from("Solen <onboarding@resend.dev>")
            .to(to)
            .subject("Please verify your email for Solen")
            .html("<p>Click <a href='" + url + "'>here</a> to verify</p>")
            .build();
        try{        
            resend.emails().send(email);
        }
        catch(ResendException e){
            throw new RuntimeException("Resend has failed");
        }
    }

    @Override
    public EmailType getType(){ return EmailType.EMAIL_VERIFICATION;}
}