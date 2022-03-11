package com.db.repo;

import com.db.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepo extends JpaRepository<Game, Integer> {
}
