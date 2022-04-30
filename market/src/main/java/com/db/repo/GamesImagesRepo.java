package com.db.repo;

import com.db.model.GamesImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesImagesRepo extends JpaRepository<GamesImage, Integer> {
  @Query(
      value = "SELECT * FROM games_images WHERE game_id = ?1 ORDER BY id LIMIT 1 OFFSET ?2",
      nativeQuery = true)
  Optional<GamesImage> findImageByGameIdWithOffset(int gameId, int offset);

  @Query(
      value =
          "DELETE FROM games_images AS gi\n"
              + "WHERE EXISTS(\n"
              + "\tSELECT * \n"
              + "\tFROM ( \n"
              + "\t\tSELECT id, ROW_NUMBER() OVER(ORDER BY id) AS num \n"
              + "\t\tFROM games_images\n"
              + "\t\tWHERE game_id = ?1 \n"
              + "\t) AS tmp\n"
              + "\tWHERE tmp.num = ?2 AND tmp.id = gi.id\n"
              + ");",
      nativeQuery = true)
  @Modifying
  void deleteGamesImageByGameIdAndNumber(Integer gameId, Integer num);
}
