package fitnesstracker.tracker.web;

import fitnesstracker.tracker.service.FitnessDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrackerRestController {
    private final FitnessDataService service;

    public TrackerRestController(FitnessDataService service) {
        this.service = service;
    }

    @PostMapping(path = "/api/tracker")
    @ResponseStatus(HttpStatus.CREATED)
    public void postData(@RequestBody DataUploadRequest request) {
        service.insertData(request);
    }

    @GetMapping(path = "/api/tracker")
    public List<DataDto> getData() {
        return service.getAllData();
    }
}
