package fitnesstracker.tracker.web;

public record DataDto(
        String id,
        String username,
        String activity,
        int duration,
        int calories
) {
}
