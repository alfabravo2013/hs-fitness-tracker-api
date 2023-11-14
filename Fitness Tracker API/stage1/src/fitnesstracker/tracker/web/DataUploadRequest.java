package fitnesstracker.tracker.web;

public record DataUploadRequest(
        String username,
        String activity,
        int duration,
        int calories
) {
}
