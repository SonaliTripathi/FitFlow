package com.fitflow.fitflow.user;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

// KEEP: /internal/** = service-to-service API, not for public clients.
// Real-world convention: the API gateway simply never routes /internal paths,
// so only other services (inside the network) can reach this.
// workout-service will call this tomorrow to verify a user exists.
@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/exists")
    public Map<String, Boolean> exists(@PathVariable Long id) {
        // Returns {"exists": true/false}. Deliberately minimal — internal APIs
        // should expose exactly what the caller needs, nothing more.
        return Map.of("exists", userService.userExists(id));
    }
}