package com.db.endpoint.userFlow;

import com.db.exception.ServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import com.db.model.dto.usersItem.UsersItemExtendedDto;
import com.db.model.dto.usersItem.UsersItemInsertDto;
import com.db.model.dto.usersItem.UsersItemUpdateDto;
import com.db.model.filter.UsersItemFilter;
import com.db.service.UsersItemsService;
import com.db.utility.filter.Filter;
import com.db.utility.filter.model.FilterResult;
import com.db.utility.mapper.ModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/user/item")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public class UsersItemsController {
  private final Filter filter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<UsersItemExtendedDto> getUsersItems(
      @RequestBody @Valid UsersItemFilter query, @Parameter(hidden = true) Authentication auth)
      throws ServiceException {
    query.setUserId((Integer) auth.getPrincipal());
    return filter.doFilter(query, UsersItemExtendedDto.class);
  }
}
