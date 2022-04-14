package com.db.endpoint.adminFlow;

import com.db.exception.CountriesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Country;
import com.db.model.dto.country.CountryInsertDto;
import com.db.model.dto.country.CountryUpdateDto;
import com.db.service.CountriesService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/country")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CountriesAdminController {
  private final CountriesService countriesService;
  private final ModelMapper modelMapper;

  @PostMapping("/existence")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  List<Boolean> checkExistence(@RequestBody List<Integer> countriesList) {
    return countriesList.stream()
        .map(countriesService::checkCountryExistence)
        .collect(Collectors.toList());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "")
  Country createCountry(@RequestBody @Valid CountryInsertDto countryDto) throws ServiceException {
    try {
      return countriesService.insertCountry(modelMapper.map(countryDto, Country.class));
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  Country updateCountry(@RequestBody @Valid CountryUpdateDto countryDto) throws ServiceException {
    try {
      return countriesService.updateCountry(modelMapper.map(countryDto, Country.class));
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "")
  void deleteCountry(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      countriesService.deleteCountry(id);
    } catch (CountriesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
