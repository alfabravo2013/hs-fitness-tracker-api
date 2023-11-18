package fitnesstracker.security;

import fitnesstracker.developer.DeveloperRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final DeveloperRepository repository;

    public AppUserDetailsService(DeveloperRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username.toLowerCase(Locale.ROOT))
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found"));
    }
}
