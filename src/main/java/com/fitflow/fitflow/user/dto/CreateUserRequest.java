package com.fitflow.fitflow.user.dto;

import com.fitflow.fitflow.user.User.FitnessGoal;
import jakarta.validation.constraints.*;

// A record = immutable data class: constructor, getters, equals/hashCode auto-generated.
// KEEP: DTO exists so the API contract can evolve separately from the DB entity —
// and so we never accidentally expose future fields like passwordHash.
public record CreateUserRequest(
        @NotBlank @Email String email,                       // must be non-empty AND email-shaped
        @NotBlank @Size(min = 2, max = 50) String displayName,
        @NotNull FitnessGoal fitnessGoal                     // must be one of the enum values
) {}
// These annotations only fire when the controller parameter has @Valid.