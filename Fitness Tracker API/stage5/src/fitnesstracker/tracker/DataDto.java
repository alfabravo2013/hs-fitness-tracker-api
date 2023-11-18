package fitnesstracker.tracker;

public record DataDto(
        String id,
        String username,
        String activity,
        int duration,
        int calories,
        String application
) {
}
