package org.solen.business.emailcases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.solen.business.emailcases.emailstrategy.EmailStrategyService;
import org.solen.business.repos.IEmailTokenRepository;
import org.solen.business.repos.IUserRepository;
import org.solen.domain.email.EmailToken;
import org.solen.domain.email.EmailType;
import org.solen.domain.users.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendEmailUseCaseImplTest {

    @Mock
    private EmailStrategyService emailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private IEmailTokenRepository tokenRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private EmailFlagHelper emailFlagHelper;

    @Mock
    private EmailRateLimiter emailRateLimiter;

    @InjectMocks
    private SendEmailUseCaseImpl sendEmailUseCase;

    @Test
    void execute_userNotFound_doesNotSendOrPersist() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(null);

        sendEmailUseCase.execute(EmailType.EMAIL_VERIFICATION, "missing@test.com");

        verify(tokenRepository, never()).save(any());
        verify(emailService, never()).send(any(), anyString(), anyString());
    }

    @Test
    void execute_rateLimited_doesNotSendOrPersist() {
        User user = User.builder().id(1L).email("user@test.com").build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(user);
        when(emailRateLimiter.isAllowed("user@test.com")).thenReturn(false);

        sendEmailUseCase.execute(EmailType.EMAIL_VERIFICATION, "user@test.com");

        verify(tokenRepository, never()).save(any());
        verify(emailService, never()).send(any(), anyString(), anyString());
    }

    @Test
    void execute_success_sendsAndPersists() {
        User user = User.builder().id(1L).email("user@test.com").build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(user);
        when(emailRateLimiter.isAllowed("user@test.com")).thenReturn(true);
        when(tokenService.generateToken()).thenReturn("abc-token");
        when(tokenRepository.save(any(EmailToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(emailFlagHelper.getFlag(EmailType.EMAIL_VERIFICATION)).thenReturn("verify=");
        when(tokenService.generateUrl("verify=", "abc-token")).thenReturn("http://localhost:3000/verify=abc-token");

        sendEmailUseCase.execute(EmailType.EMAIL_VERIFICATION, "user@test.com");

        verify(tokenRepository, times(1)).save(any(EmailToken.class));
        verify(emailService, times(1)).send(EmailType.EMAIL_VERIFICATION, "user@test.com", "http://localhost:3000/verify=abc-token");
    }
}
