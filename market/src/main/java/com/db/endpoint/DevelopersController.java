package com.db.endpoint;

import com.db.exception.DevelopersServiceException;
import com.db.exception.ServiceException;
import com.db.model.Developer;
import com.db.model.filter.DeveloperFilter;
import com.db.service.DevelopersService;
import com.db.utility.filter.Filter;
import com.db.utility.filter.model.FilterResult;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/developer")
@RequiredArgsConstructor
@Validated
public class DevelopersController {

  private final DevelopersService developersService;
  private final Filter filter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<Developer> getDevelopers(@RequestBody @Valid DeveloperFilter query)
      throws ServiceException {
    return filter.doFilter(query, Developer.class);
  }

  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation
  Developer getDeveloper(@PathVariable @Min(value = 1, message = ConstraintMessages.MIN) int id)
      throws ServiceException {
    try {
      return developersService.findDeveloperById(id);
    } catch (DevelopersServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
