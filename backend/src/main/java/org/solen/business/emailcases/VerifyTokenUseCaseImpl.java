package org.solen.business.emailusecases;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VerifyTokenUseCaseImpl implements IVerifyTokenUseCase{

    private final TokenService tokenService; 

    @Override
    public Boolean verifyToken(String url) {
        return true;
        
    }
    
}
