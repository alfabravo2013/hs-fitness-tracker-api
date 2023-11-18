package fitnesstracker.application;

import fitnesstracker.security.SecurityUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ApplicationRestController {
    private final ApplicationService service;

    public ApplicationRestController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping(path = "/api/applications/register")
    public ResponseEntity<ApiKeyDto> register(@Valid @RequestBody ApplicationRegistrationRequest request,
                                                   @AuthenticationPrincipal SecurityUser user) {
        var payload = service.registerApplication(request, user.getDeveloper());
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    @PostMapping(path = "/api/applications/{id}/apikey")
    @PreAuthorize("@dm.decide(#user, #id)")
    public ResponseEntity<ApiKeyDto> recreateApiKey(@PathVariable String id,
                               @AuthenticationPrincipal SecurityUser user) {
        var payload = service.regenerateApiKey(id, user.getDeveloper());
        return ResponseEntity.ok(payload);
    }
}
