package fitnesstracker.developer;

import fitnesstracker.application.Application;
import fitnesstracker.application.ApplicationProfile;
import fitnesstracker.common.FitnessTrackerApiException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
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
            entity.setApplications(new HashSet<>(0));
            return repository.save(entity).getId();
        } catch (DataIntegrityViolationException e) {
            throw new FitnessTrackerApiException("Email already taken");
        }
    }

    @Transactional
    public void updateDeveloper(Developer developer) {
        repository.save(developer);
    }

    public DeveloperProfile getProfile(String id) {
        var developer = repository.findById(id)
                .orElseThrow(() -> new FitnessTrackerApiException("Profile not found"));

        return new DeveloperProfile(
                developer.getId(),
                developer.getEmail(),
                developer.getApplications().stream()
                        .sorted(Comparator.comparing(Application::getTimestamp).reversed())
                        .map(app -> new ApplicationProfile(
                                        app.getId(),
                                        app.getName(),
                                        app.getDescription(),
                                        app.getApiKey(),
                                        app.getCategory()
                                )
                        ).toList()
        );
    }
}
