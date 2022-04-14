package com.db.service;

import com.db.exception.CountriesServiceException;
import com.db.model.Country;
import com.db.repo.CountriesRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountriesService {
  private final CountriesRepo countriesRepo;

  @Transactional(readOnly = true)
  public List<Country> getCountries() {
    return countriesRepo.findAll();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Country insertCountry(Country country) throws CountriesServiceException {
    if (countriesRepo.existsById(country.getId())) {
      throw new CountriesServiceException(CountriesServiceException.COUNTRY_ALREADY_EXISTS);
    }

    try {
      return countriesRepo.save(country);
    } catch (DataAccessException ex) {
      throw new CountriesServiceException(ex.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public boolean checkCountryExistence(int id) {
    return countriesRepo.existsById(id);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Country updateCountry(Country country) throws CountriesServiceException {
    if (countriesRepo.existsById(country.getId())) {
      throw new CountriesServiceException(CountriesServiceException.COUNTRY_NOT_FOUND);
    }

    try {
      return countriesRepo.save(country);
    } catch (DataAccessException ex) {
      throw new CountriesServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteCountry(int id) throws CountriesServiceException {
    try {
      countriesRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new CountriesServiceException(ex.getMessage());
    }
  }
}
