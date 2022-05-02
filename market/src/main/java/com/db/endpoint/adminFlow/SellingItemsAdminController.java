package com.db.endpoint.adminFlow;

import com.db.exception.ServiceException;
import com.db.model.dto.sellingItem.SellingItemExtendedDto;
import com.db.model.filter.SellingItemFilter;
import com.db.utility.sql.filter.SqlFilter;
import com.db.utility.sql.filter.model.FilterResult;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/selling")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class SellingItemsAdminController {
  private final SqlFilter sqlFilter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<SellingItemExtendedDto> getSellingItems(@Valid SellingItemFilter query)
      throws ServiceException {
    return sqlFilter.doFilter(query, SellingItemExtendedDto.class);
  }
}
