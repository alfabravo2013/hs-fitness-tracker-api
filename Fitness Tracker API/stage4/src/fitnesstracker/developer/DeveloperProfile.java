package fitnesstracker.developer;

import fitnesstracker.application.ApplicationProfile;

import java.util.List;

public record DeveloperProfile(
        String id,
        String email,
        List<ApplicationProfile> applications
) {
}
