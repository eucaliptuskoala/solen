package org.solen.business.emailusecases;

import java.time.LocalDateTime;

import org.solen.business.emailusecases.emailstrategy.EmailStrategyService;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.repos.IEmailTokenRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.email.EmailToken;
import org.solen.domain.email.EmailType;
import org.solen.domain.users.User;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SendEmailUseCaseImpl implements ISendEmailUseCaseImpl{

    private final EmailStrategyService emailService;
    private final TokenService tokenService;
    private final IEmailTokenRepository tokenRepository;
    private final IUserRepository userRepository;
    private final EmailFlagHelper emailFlagHelper;
    
    @Override
    public void execute(EmailType type, String email) {
        String token = tokenService.generateToken();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundByEmailException(email);
        }

        EmailToken emailToken = tokenRepository.save(createToken(user, token));
        String flag = emailFlagHelper.getFlag(type);

        String url = tokenService.generateUrl(flag, emailToken.getToken());
        emailService.send(type, user.getEmail(), url);
    }

    private EmailToken createToken(User user, String token){
        return EmailToken.builder()
        .user(user)
        .token(token)
        .expiration(LocalDateTime.now().plusMinutes(30))
        .build();
    }
}
