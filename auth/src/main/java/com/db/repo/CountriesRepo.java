package com.db.repo;

import com.db.model.Country;
import com.db.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepo extends JpaRepository<Country, Integer> {
}
