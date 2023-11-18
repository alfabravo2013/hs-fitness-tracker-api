package fitnesstracker.tracker;

import fitnesstracker.application.Application;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class TrackerRestController {
    private final FitnessDataService service;
    private final RateLimiter rateLimiter;

    public TrackerRestController(FitnessDataService service, RateLimiter rateLimiter) {
        this.service = service;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping(path = "/api/tracker")
    @ResponseStatus(HttpStatus.CREATED)
    public void postData(@RequestBody DataUploadRequest request,
                         @AuthenticationPrincipal Application application) {
        tryMakeRequest(application);
        service.insertData(request, application);
    }

    @GetMapping(path = "/api/tracker")
    public List<DataDto> getData(@AuthenticationPrincipal Application application) {
        tryMakeRequest(application);
        return service.getAllData();
    }

    private void tryMakeRequest(Application application) {
        var isGranted = true;
        if (Objects.equals("basic", application.getCategory())) {
            isGranted = rateLimiter.isGranted(application.getApiKey());
        }

        if (!isGranted) {
            throw new RateLimitExceededException();
        }
    }
}
