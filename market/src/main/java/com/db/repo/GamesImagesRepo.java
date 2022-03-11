package com.db.repo;

import com.db.model.GamesImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesImagesRepo extends JpaRepository<GamesImage, Integer> {
}
