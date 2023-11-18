package fitnesstracker.security;

import fitnesstracker.application.ApplicationRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private final ApplicationRepository repository;

    public ApiKeyAuthenticationProvider(ApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = authentication.getCredentials().toString();

        var application = repository
                .findByApiKey(apiKey)
                .orElseThrow(() -> new BadCredentialsException("Unknown api key"));

        var apiKeyAuthentication = new ApiKeyAuthentication(application, apiKey);
        apiKeyAuthentication.setAuthenticated(true);
        return apiKeyAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentication.class.equals(authentication);
    }
}
