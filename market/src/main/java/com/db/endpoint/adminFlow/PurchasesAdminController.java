package com.db.endpoint.adminFlow;

import com.db.exception.ServiceException;
import com.db.model.Purchase;
import com.db.model.filter.PurchaseFilter;
import com.db.utility.sql.filter.SqlFilter;
import com.db.utility.sql.filter.model.FilterResult;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/purchase")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class PurchasesAdminController {

  private final SqlFilter sqlFilter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<Purchase> getPurchases(@RequestBody @Valid PurchaseFilter query)
      throws ServiceException {
    return sqlFilter.doFilter(query, Purchase.class);
  }
}
