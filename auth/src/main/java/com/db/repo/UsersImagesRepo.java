package com.db.repo;

import com.db.model.UsersImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersImagesRepo extends JpaRepository<UsersImage, Integer> {
}
