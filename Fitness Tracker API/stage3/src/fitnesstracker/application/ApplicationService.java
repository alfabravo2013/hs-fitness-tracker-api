package fitnesstracker.application;

import fitnesstracker.common.FitnessTrackerApiException;
import fitnesstracker.developer.Developer;
import fitnesstracker.developer.DeveloperService;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class ApplicationService {
    private final DeveloperService developerService;

    public ApplicationService(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @Transactional
    public ApiKeyDto registerApplication(ApplicationRegistrationRequest request,
                                              Developer developer) {
        boolean isNonUniqueAppName = developer.getApplications().stream()
                .map(Application::getName)
                .anyMatch(appName -> appName.equals(request.name()));
        if (isNonUniqueAppName) {
            throw new FitnessTrackerApiException("Application with this name already exists");
        }

        var apikey = generateApiKey();
        var application = new Application();

        application.setName(request.name());
        application.setDescription(request.description());
        application.setApikey(apikey);
        application.setDeveloper(developer);

        developer.getApplications().add(application);
        developerService.updateDeveloper(developer);

        return new ApiKeyDto(
                application.getName(),
                application.getApikey()
        );
    }

    @Transactional
    public ApiKeyDto regenerateApiKey(String id, Developer developer) {
        var application = developer.getApplications().stream()
                .filter(app -> app.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new FitnessTrackerApiException("Application not found"));

        developer.getApplications().remove(application);
        var apikey = generateApiKey();
        application.setApikey(apikey);
        developer.getApplications().add(application);
        developerService.updateDeveloper(developer);

        return new ApiKeyDto(application.getName(), apikey);
    }

    private String generateApiKey() {
        var bytes = KeyGenerators.secureRandom(20).generateKey();
        return new BigInteger(1, bytes).toString(16);
    }
}
