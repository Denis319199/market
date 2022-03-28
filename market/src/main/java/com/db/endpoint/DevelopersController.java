package com.db.endpoint;

import com.db.exception.DevelopersServiceException;
import com.db.exception.ServiceException;
import com.db.model.Developer;
import com.db.model.dto.developer.DeveloperDto;
import com.db.model.dto.developer.DeveloperInsertDto;
import com.db.model.dto.developer.DeveloperExtendedUpdateDto;
import com.db.service.DevelopersService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
@Validated
public class DevelopersController {

  private final DevelopersService developersService;
  private final ModelMapper modelMapper;

  @GetMapping
  List<DeveloperDto> getDevelopers(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
    return developersService.getAllDevelopers(page, size).stream()
        .map(developer -> modelMapper.map(developer, DeveloperDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}")
  DeveloperDto getDeveloper(@PathVariable @Min(1) int id) throws ServiceException {
    try {
      return modelMapper.map(developersService.findDeveloperById(id), DeveloperDto.class);
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  DeveloperDto insertDeveloper(@RequestBody @Valid DeveloperInsertDto developerDto)
          throws ServiceException {
    try {
      Developer developer =
              developersService.insertDeveloper(modelMapper.map(developerDto, Developer.class));
      return modelMapper.map(developer, DeveloperDto.class);
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  DeveloperDto updateDeveloper(@RequestBody @Valid DeveloperExtendedUpdateDto developerDto)
          throws ServiceException {
    try {
      Developer developer =
              developersService.updateDeveloper(modelMapper.map(developerDto, Developer.class));
      return modelMapper.map(developer, DeveloperDto.class);
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  void deleteDeveloper(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      developersService.deleteDeveloper(id);
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
