package fitnesstracker.application;

import fitnesstracker.security.SecurityUser;
import org.springframework.stereotype.Component;

@Component("dm")
public class ApiKeyDecisionMaker {

    public boolean decide(SecurityUser user, String appId) {
        if (user == null) {
            return false;
        }

        return user.getDeveloper().getApplications().stream()
                .anyMatch(app -> app.getId().equals(appId));
    }
}
