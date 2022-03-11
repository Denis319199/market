package com.db.repo;

import com.db.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevelopersRepo extends JpaRepository<Developer, Integer> {
}
