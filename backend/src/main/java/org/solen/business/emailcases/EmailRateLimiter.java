package org.solen.business.emailcases;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailRateLimiter {

    private final Map<String, Instant> lastSent = new ConcurrentHashMap<>();
    private final Duration cooldown;

    public EmailRateLimiter(@Value("${email.cooldown.seconds:60}") long cooldownSeconds) {
        this.cooldown = Duration.ofSeconds(cooldownSeconds);
    }

    public boolean isAllowed(String email) {
        Instant now = Instant.now();
        Instant last = lastSent.get(email);
        if (last == null || last.plus(cooldown).isBefore(now)) {
            lastSent.put(email, now);
            return true;
        }
        return false;
    }
}
