package fitnesstracker.developer;

import fitnesstracker.security.SecurityUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class DeveloperRestController {
    private final DeveloperService service;

    public DeveloperRestController(DeveloperService service) {
        this.service = service;
    }

    @PostMapping(path = "/api/developers/signup")
    public ResponseEntity<Void> register(@Valid @RequestBody DeveloperRegistrationRequest request) {
        var id = service.registerDeveloper(request);
        return ResponseEntity
                .created(URI.create("/api/developers/" + id))
                .build();
    }

    @GetMapping(path = "/api/developers/{id}")
    @PreAuthorize("#user.developer.id == #id")
    public ResponseEntity<DeveloperProfile> getProfile(@PathVariable String id,
                                                       @AuthenticationPrincipal SecurityUser user) {
        var payload = service.getProfile(id);
        return ResponseEntity.ok(payload);
    }
}
