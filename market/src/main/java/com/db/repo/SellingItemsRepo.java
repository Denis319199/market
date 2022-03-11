package com.db.repo;

import com.db.model.SellingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellingItemsRepo extends JpaRepository<SellingItem, Integer> {
}
