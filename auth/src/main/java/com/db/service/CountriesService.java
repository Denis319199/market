package com.db.service;

import com.db.exception.CountriesServiceException;
import com.db.model.Country;
import com.db.repo.CountriesRepo;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Optional;
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

  @Transactional(readOnly = true)
  public Country findCountryById(int id) throws CountriesServiceException {
    Optional<Country> country = countriesRepo.findById(id);

    if (country.isEmpty()) {
      throw new CountriesServiceException(CountriesServiceException.COUNTRY_NOT_FOUND);
    }

    return country.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Country insertCountry(Country country) throws CountriesServiceException {
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
    Country old = findCountryById(country.getId());
    Utilities.merge(country, old);

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
