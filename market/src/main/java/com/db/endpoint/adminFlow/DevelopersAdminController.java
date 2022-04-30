package com.db.endpoint.adminFlow;

import com.db.exception.DevelopersServiceException;
import com.db.exception.ServiceException;
import com.db.model.Developer;
import com.db.model.dto.developer.DeveloperInsertDto;
import com.db.model.dto.developer.DeveloperUpdateDto;
import com.db.service.DevelopersService;
import com.db.utility.mapper.ModelMapper;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/admin/developer")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class DevelopersAdminController {
  private final DevelopersService developersService;
  private final ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation
  Developer insertDeveloper(@RequestBody @Valid DeveloperInsertDto developerDto)
      throws ServiceException {
    try {
      return developersService.insertDeveloper(modelMapper.map(developerDto, Developer.class));
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  Developer updateDeveloper(@RequestBody @Valid DeveloperUpdateDto developerDto)
      throws ServiceException {
    try {
      return developersService.updateDeveloper(modelMapper.map(developerDto, Developer.class));
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  void deleteDeveloper(@RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int id)
      throws ServiceException {
    try {
      developersService.deleteDeveloper(id);
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
