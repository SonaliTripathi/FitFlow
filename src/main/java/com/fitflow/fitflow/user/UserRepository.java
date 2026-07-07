package com.fitflow.fitflow.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Extending JpaRepository<EntityType, IdType> gives us save(), findById(),
// findAll(), existsById(), delete()... for free. Spring generates the
// implementation at runtime — no SQL, no impl class.
public interface UserRepository extends JpaRepository<User, Long> {

    // DERIVED QUERIES: Spring parses the METHOD NAME and generates SQL.
    // findByEmail -> SELECT * FROM users WHERE email = ?
    // Optional<> forces callers to handle the "not found" case explicitly.
    Optional<User> findByEmail(String email);

    // existsByEmail -> SELECT COUNT(*) > 0 ... — cheaper than fetching the row.
    boolean existsByEmail(String email);
}