package org.solen.configuration;

import org.solen.business.repos.IUserRepository;
import org.solen.domain.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@solen.app}")
    private String adminEmail;

    @Value("${admin.password:}")
    private String adminPassword;

    public AdminBootstrapRunner(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        boolean adminExists = userRepository.findAll().stream().anyMatch(User::isAdmin);
        if (!adminExists && !adminPassword.isBlank()) {
            userRepository.save(User.builder()
                    .name("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .isAdmin(true)
                    .build());
        }
    }
}
