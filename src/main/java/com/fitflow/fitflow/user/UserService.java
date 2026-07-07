package com.fitflow.fitflow.user;

import com.fitflow.fitflow.user.dto.CreateUserRequest;
import com.fitflow.fitflow.user.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

// @Service registers this class as a Spring bean (singleton) so it can be
// injected into controllers. Business logic lives HERE — controllers stay thin.
@Service
public class UserService {

    // final = must be set in the constructor, can never be swapped afterwards.
    private final UserRepository userRepository;

    // CONSTRUCTOR INJECTION: Spring sees this is the only constructor and
    // automatically passes in the UserRepository bean — no @Autowired needed.
    // Why this over field injection: field can be final, and in a plain JUnit
    // test you can do new UserService(mockRepo) without any Spring magic.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {
        // Friendly pre-check for duplicates -> clean 409 via the handler.
        // NOTE: the DB unique constraint is the REAL guard (two simultaneous
        // requests could both pass this check — the constraint catches the loser).
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = new User(request.email(), request.displayName(), request.fitnessGoal());

        // save() INSERTs and returns the entity WITH the DB-generated id —
        // that's why we map the returned object, not our local 'user'.
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse getUser(Long id) {
        // findById returns Optional<User> — orElseThrow converts the empty
        // case into an exception the GlobalExceptionHandler maps to 404.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        // stream().map(...) converts each entity to a DTO; method reference
        // this::toResponse is shorthand for u -> toResponse(u).
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Used by InternalUserController — workout-service calls this tomorrow.
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    // ONE place that maps entity -> DTO. If UserResponse gains a field,
    // there's exactly one line to update.
    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getDisplayName(),
                u.getFitnessGoal(), u.getCreatedAt());
    }
}