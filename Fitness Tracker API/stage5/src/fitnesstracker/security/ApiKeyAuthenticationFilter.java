package fitnesstracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);
    private final RequestMatcher matcher = new AntPathRequestMatcher("/api/tracker");
    private final AuthenticationEntryPoint authenticationEntryPoint = (request, response, ex) -> {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(ex.getMessage());
    };
    private final AuthenticationManager manager;

    public ApiKeyAuthenticationFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (matcher.matches(request)) {
            log.info("Request matched!");
            var apiKey = request.getHeader("X-API-Key");
            log.info("Got api key: {}", apiKey);
            try {
                if (apiKey != null) {
                    Authentication authentication = new ApiKeyAuthentication(null, apiKey);
                    authentication = manager.authenticate(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
                throw new BadCredentialsException("No api key");
            } catch (AuthenticationException e) {
                authenticationEntryPoint.commence(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
