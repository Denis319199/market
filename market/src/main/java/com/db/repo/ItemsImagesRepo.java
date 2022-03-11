package com.db.repo;

import com.db.model.ItemsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsImagesRepo extends JpaRepository<ItemsImage, Integer> {
}
