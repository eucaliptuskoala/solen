package org.solen.business.signincases;

import lombok.AllArgsConstructor;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.repos.IUserRepository;
import org.solen.configuration.security.JwtUtil;
import org.solen.controller.dto.auth.SignInRequest;
import org.solen.controller.dto.auth.SignInResponse;
import org.solen.domain.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SignInUseCaseImpl implements ISignInUseCase {

    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail());

        if (user == null || !passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new UserNotFoundByEmailException(signInRequest.getEmail());
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getName());

        return SignInResponse.builder().token(token).build();
    }
}