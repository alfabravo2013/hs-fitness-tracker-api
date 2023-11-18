package fitnesstracker.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FitnessTrackerApiException extends RuntimeException {
    public FitnessTrackerApiException(String message) {
        super(message);
    }
}
