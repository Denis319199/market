package com.db.repo;

import com.db.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByIdAndIsEnabled(Integer id, Boolean isEnabled);
}
