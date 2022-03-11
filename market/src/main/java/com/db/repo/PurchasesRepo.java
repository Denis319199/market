package com.db.repo;

import com.db.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasesRepo extends JpaRepository<Purchase, Integer> {}
