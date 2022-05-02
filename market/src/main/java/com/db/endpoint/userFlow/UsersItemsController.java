package com.db.endpoint.userFlow;

import com.db.exception.ServiceException;
import com.db.model.dto.usersItem.UsersItemExtendedDto;
import com.db.model.filter.UsersItemFilter;
import com.db.utility.sql.filter.SqlFilter;
import com.db.utility.sql.filter.model.FilterResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/item")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public class UsersItemsController {
  private final SqlFilter sqlFilter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<UsersItemExtendedDto> getUsersItems(
      @Valid UsersItemFilter query, @Parameter(hidden = true) Authentication auth)
      throws ServiceException {
    query.setUserId((Integer) auth.getPrincipal());
    return sqlFilter.doFilter(query, UsersItemExtendedDto.class);
  }
}
