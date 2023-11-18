package fitnesstracker.tracker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    private static final int MAX_TOKENS = 1;
    private static final int INITIAL_TOKENS = 1;
    private static final long REPLENISH_COOLDOWN = 1000;

    private final Map<String, Integer> tokenBukets = new ConcurrentHashMap<>();

    public boolean isGranted(String apiKey) {
        tokenBukets.putIfAbsent(apiKey, INITIAL_TOKENS);
        var attempts = tokenBukets.get(apiKey);
        tokenBukets.computeIfPresent(apiKey, (k, v) -> Math.max(attempts - 1, 0));
        return attempts > 0;
    }

    @Scheduled(fixedDelay = REPLENISH_COOLDOWN)
    private void replenish() {
        for (var entry : tokenBukets.entrySet()) {
            if (entry.getValue() < MAX_TOKENS) {
                tokenBukets.put(entry.getKey(), entry.getValue() + 1);
            }
        }
    }
}
