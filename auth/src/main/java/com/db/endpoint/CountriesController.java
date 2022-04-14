package com.db.endpoint;

import com.db.exception.CountriesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Country;
import com.db.service.CountriesService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
@Validated
public class CountriesController {
  private final CountriesService countriesService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  public List<Country> getCountries() {
    return countriesService.getCountries();
  }

  @GetMapping(value = "/{countryId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  public Country getCountry(@PathVariable @Min(1) int countryId) throws ServiceException {
    try {
      return countriesService.findCountryById(countryId);
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
