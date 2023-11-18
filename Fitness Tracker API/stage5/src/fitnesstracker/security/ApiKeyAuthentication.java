package fitnesstracker.security;

import fitnesstracker.application.Application;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class ApiKeyAuthentication implements Authentication {
    private final Application application;
    private final String apiKey;
    private boolean authenticated;

    public ApiKeyAuthentication(Application application, String apiKey) {
        this.application = application;
        this.apiKey = apiKey;
        this.authenticated = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "application");
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return application;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return application.getName();
    }
}
