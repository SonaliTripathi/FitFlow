package com.fitflow.fitflow.user;

import com.fitflow.fitflow.user.dto.CreateUserRequest;
import com.fitflow.fitflow.user.dto.UserResponse;
import jakarta.validation.Valid;                       // careful: jakarta, not javax (old tutorials use javax)
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody: return values are serialized
// to JSON automatically. The controller's ONLY jobs: bind HTTP -> Java,
// delegate to the service, pick the status code. No business logic here.
@RestController
@RequestMapping("/api/v1/users")   // versioned from day one: breaking changes later go to /v2
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {   // same injection pattern as the service
        this.userService = userService;
    }

    // POST /api/v1/users
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        // @RequestBody: JSON body -> CreateUserRequest record
        // @Valid: run the record's constraints (@Email, @NotBlank...);
        //         failure throws MethodArgumentNotValidException -> handler -> 400
        UserResponse created = userService.createUser(request);

        // 201 Created is the semantically correct status for "resource made" —
        // returning 200 here is a classic API-design nitpick interviewers notice.
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/v1/users/{id}
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        // @PathVariable binds the {id} URL segment to the parameter.
        // Returning the DTO directly = implicit 200 OK. The not-found case
        // never reaches here — service throws, handler returns 404.
        return userService.getUser(id);
    }

    // GET /api/v1/users
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }
}