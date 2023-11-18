package fitnesstracker.tracker;

public record DataUploadRequest(
        String username,
        String activity,
        int duration,
        int calories
) {
}
