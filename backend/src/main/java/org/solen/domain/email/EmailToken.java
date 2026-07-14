package org.solen.domain.email;

import java.time.LocalDateTime;

import org.solen.domain.users.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailToken {
    private Long id;
    private User user;
    private String token;
    private LocalDateTime expiration;
}
