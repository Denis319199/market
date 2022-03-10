package com.db.service;

import com.db.exception.ServiceException;
import com.db.model.Country;
import com.db.repo.CountriesRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
    public Country insert(Country country) throws ServiceException {
        if (countriesRepo.existsById(country.getId())) {
            throw new ServiceException(ServiceException.COUNTRY_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        return countriesRepo.save(country);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Country save(Country country) throws ServiceException {
        if (countriesRepo.existsById(country.getId())) {
            throw new ServiceException(ServiceException.COUNTRY_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        return countriesRepo.save(country);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void delete(int id) {
        countriesRepo.deleteById(id);
    }
}
