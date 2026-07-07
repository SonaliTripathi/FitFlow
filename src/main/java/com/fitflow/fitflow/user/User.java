package com.fitflow.fitflow.user;

import jakarta.persistence.*;   // JPA annotations: Entity, Id, Column, etc.
import java.time.Instant;       // Instant = a UTC timestamp; always store times in UTC

// @Entity tells Hibernate: this class maps to a database table.
// On startup (because ddl-auto=update) Hibernate will CREATE this table if missing.
@Entity
// KEEP: "user" is a RESERVED WORD in Postgres — naming the table "users" avoids
// quoting hell in every raw SQL query. Classic real-world gotcha.
@Table(name = "users")
public class User {

    @Id  // primary key
    // IDENTITY = let Postgres generate the id via its native auto-increment.
    // (Alternative: SEQUENCE — gives batching benefits; IDENTITY is simplest.)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique=true creates a DB-level UNIQUE constraint — the real guard against
    // duplicate emails. The service-layer check we add later is just for a
    // friendly error message; the DB constraint is the source of truth.
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String displayName;

    // KEEP: STRING stores "MUSCLE_GAIN" text in the DB. The default (ORDINAL)
    // stores 0/1/2/3 — then reordering the enum in code silently corrupts
    // existing data. Never use ORDINAL.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitnessGoal fitnessGoal;

    // Nullable on purpose — user may not have set targets yet.
    private Integer dailyCalorieTarget;
    private Integer dailyProteinTargetGrams;

    // updatable=false: even if code accidentally modifies this field,
    // Hibernate will never include it in an UPDATE statement.
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public enum FitnessGoal { WEIGHT_LOSS, MUSCLE_GAIN, ENDURANCE, GENERAL_FITNESS }

    // KEEP: Hibernate creates entities via reflection and needs a no-arg
    // constructor. 'protected' so application code can't misuse it.
    protected User() {}

    // The constructor application code actually uses.
    public User(String email, String displayName, FitnessGoal fitnessGoal) {
        this.email = email;
        this.displayName = displayName;
        this.fitnessGoal = fitnessGoal;
    }

    // Getters (no setter for id/email/createdAt — they shouldn't change after creation)
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public FitnessGoal getFitnessGoal() { return fitnessGoal; }
    public Integer getDailyCalorieTarget() { return dailyCalorieTarget; }
    public Integer getDailyProteinTargetGrams() { return dailyProteinTargetGrams; }
    public Instant getCreatedAt() { return createdAt; }
    public void setDailyCalorieTarget(Integer v) { this.dailyCalorieTarget = v; }
    public void setDailyProteinTargetGrams(Integer v) { this.dailyProteinTargetGrams = v; }
}