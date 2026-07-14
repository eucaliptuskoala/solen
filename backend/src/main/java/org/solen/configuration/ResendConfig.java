package org.solen.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.resend.Resend;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class ResendConfig {

    @Value("${RESEND_TOKEN}")
    private String apiKey;

    @Bean
    public Resend resend(){
        return new Resend(apiKey);
    }
}
