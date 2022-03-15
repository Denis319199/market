package com.db.repo;

import com.db.model.GamesImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesImagesRepo extends JpaRepository<GamesImage, Integer> {
  @Query(
      value = "SELECT * FROM games_images WHERE game_id = ?1 LIMIT 1 OFFSET ?2",
      nativeQuery = true)
  Optional<GamesImage> findImageByGameIdWithOffset(int gameId, int offset);
}
