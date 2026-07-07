package com.fitflow.fitflow.user.dto;

import com.fitflow.fitflow.user.User.FitnessGoal;
import java.time.Instant;

// What we send OUT. Note: no password-ish fields, ever.
public record UserResponse(Long id, String email, String displayName,
                           FitnessGoal fitnessGoal, Instant createdAt) {}