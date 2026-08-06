package org.solen.business.emailusecases.emailstrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.solen.domain.email.EmailType;
import org.springframework.stereotype.Service;

@Service
public class EmailStrategyService {
    private final Map<EmailType, IEmailStrategy> strategies;
    
    public EmailStrategyService(List<IEmailStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(IEmailStrategy::getType, s -> s));
    }

    public void send(EmailType type, String to, String url) {
        IEmailStrategy strategy = strategies.get(type);
        if (strategy == null) throw new IllegalArgumentException("Unknown email type: " + type);
        strategy.send(to, url);
    }
}
