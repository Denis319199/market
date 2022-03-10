package com.db.repo;

import com.db.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokensRepo extends JpaRepository<RefreshToken, String> {

    @Query(value = "SELECT * FROM refresh_tokens WHERE user_id = ?1", nativeQuery = true)
    Optional<RefreshToken> findByUserId(Integer userId);

    @Query(value = "SELECT COUNT(*) FROM refresh_tokens WHERE user_id = ?1", nativeQuery = true)
    Integer countUsersTokens(Integer user_id);

    @Query(value = "DELETE FROM refresh_tokens WHERE expires_on <= NOW()", nativeQuery = true)
    @Modifying
    void deleteAllExpiredTokens();
}
