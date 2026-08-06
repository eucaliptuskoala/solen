package org.solen.business.emailusecases;

import java.time.LocalDateTime;
import java.util.UUID;

import org.solen.business.exceptions.TokenNotFoundException;
import org.solen.business.repos.IEmailTokenRepository;
import org.solen.domain.email.EmailToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

    private final String frontendUrl;
    private final IEmailTokenRepository tokenRepository;

    public TokenService(@Value("${frontend.url}") String frontendUrl, IEmailTokenRepository tokenRepository) {
        this.frontendUrl = frontendUrl;
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }

    public String generateUrl(String flag, String token){
        return frontendUrl + flag + token;
    }

    public Boolean verifyToken(String token){
        EmailToken emailToken = tokenRepository.findByToken(token);
        if(emailToken != null){
            LocalDateTime now = LocalDateTime.now();
            int difference = now.compareTo(emailToken.getExpiration());
            if(difference <= 0){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            throw new TokenNotFoundException(token);
        }
    } 
}
