package fitnesstracker.developer;

import fitnesstracker.common.FitnessTrackerApiException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DeveloperService {
    private final DeveloperRepository repository;
    private final PasswordEncoder encoder;

    public DeveloperService(DeveloperRepository repository,
                            PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public String registerDeveloper(DeveloperRegistrationRequest request) {
        try {
            var entity = new Developer();
            entity.setEmail(request.email().toLowerCase(Locale.ROOT));
            entity.setPassword(encoder.encode(request.password()));
            return repository.save(entity).getId();
        } catch (DataIntegrityViolationException e) {
            throw new FitnessTrackerApiException("Email already taken");
        }
    }

    public DeveloperProfile getProfile(String id) {
        var developer = repository.findById(id)
                .orElseThrow(() -> new FitnessTrackerApiException("Profile not found"));

        return new DeveloperProfile(developer.getId(), developer.getEmail());
    }
}
