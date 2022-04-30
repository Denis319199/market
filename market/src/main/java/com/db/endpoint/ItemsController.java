package com.db.endpoint;

import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.Item;
import com.db.model.filter.ItemFilter;
import com.db.service.ItemsService;
import com.db.utility.sql.filter.SqlFilter;
import com.db.utility.sql.filter.model.FilterResult;
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
@Validated
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemsController {
  private final ItemsService itemsService;
  private final SqlFilter sqlFilter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<Item> getItems(@RequestBody @Valid ItemFilter query) throws ServiceException {
    return sqlFilter.doFilter(query, Item.class);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public Item getItem(@PathVariable @Min(value = 1, message = ConstraintMessages.MIN) int id)
      throws ServiceException {
    try {
      return itemsService.findItemById(id);
    } catch (ItemsServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
