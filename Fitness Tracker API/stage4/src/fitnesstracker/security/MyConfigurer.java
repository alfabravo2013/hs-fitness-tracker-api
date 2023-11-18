package fitnesstracker.security;

import fitnesstracker.application.ApplicationRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class MyConfigurer extends AbstractHttpConfigurer<MyConfigurer, HttpSecurity> {
    private final ApplicationRepository repository;

    public MyConfigurer(ApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager manager = builder.getSharedObject(AuthenticationManager.class);
        builder
                .addFilterAfter(
                        new ApiKeyAuthenticationFilter(manager),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authenticationProvider(new ApiKeyAuthenticationProvider(repository));
    }
}
