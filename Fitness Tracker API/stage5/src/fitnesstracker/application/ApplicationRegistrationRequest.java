package fitnesstracker.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ApplicationRegistrationRequest(
        @NotBlank
        String name,
        @NotNull
        String description,
        @NotBlank
        @Pattern(regexp = "(basic|premium)")
        String category
) {
}
