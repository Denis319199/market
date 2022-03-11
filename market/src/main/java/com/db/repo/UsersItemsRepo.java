package com.db.repo;

import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersItemsRepo extends JpaRepository<UsersItem, UsersItemId> {}
