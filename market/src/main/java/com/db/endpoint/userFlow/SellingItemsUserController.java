package com.db.endpoint.userFlow;

import com.db.exception.ServiceException;
import com.db.model.dto.sellingItem.SellingItemExtendedDto;
import com.db.model.filter.SellingItemFilter;
import com.db.utility.filter.Filter;
import com.db.utility.filter.model.FilterResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/item/selling")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public class SellingItemsUserController {
  private final Filter filter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<SellingItemExtendedDto> getSellingItems(
      @RequestBody @Valid SellingItemFilter query, @Parameter(hidden = true) Authentication auth)
      throws ServiceException {
    query.setSellerId((Integer) auth.getPrincipal());
    return filter.doFilter(query, SellingItemExtendedDto.class);
  }
}