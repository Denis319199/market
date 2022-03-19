package com.db.endpoint;

import com.db.exception.CountriesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Country;
import com.db.model.dto.CountryDto;
import com.db.model.dto.CountryUpdateDto;
import com.db.service.CountriesService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
@Validated
public class CountriesController {
  private final CountriesService countriesService;
  private final ModelMapper modelMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<Country> getCountries() {
    return countriesService.getCountries();
  }

  @PostMapping("/existence")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  List<Boolean> checkExistence(@RequestBody List<Integer> countriesList) {
    return countriesList.stream()
        .map(countriesService::checkExistence)
        .collect(Collectors.toList());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  Country createCountry(@RequestBody @Valid CountryDto countryDto) throws ServiceException {
    try {
      return countriesService.insert(modelMapper.map(countryDto, Country.class));
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  Country updateCountry(@RequestBody @Valid CountryUpdateDto countryDto) throws ServiceException {
    try {
      return countriesService.save(modelMapper.map(countryDto, Country.class));
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  void deleteUser(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      countriesService.delete(id);
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
