package com.db.model.filter;

import com.db.model.Purchase;
import com.db.utility.sql.filter.model.Operation;
import com.db.utility.sql.filter.annotation.FilterModel;
import com.db.utility.sql.filter.annotation.FilterOperation;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.annotation.MinArray;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

@FilterModel(Purchase.class)
@Data
@NoArgsConstructor
public class PurchaseFilter {
  @FilterOperation(op = Operation.EQUAL)
  @MinArray(value = 1, message = ConstraintMessages.MIN)
  private Integer[] sellerId;

  @FilterOperation(op = Operation.EQUAL)
  @MinArray(value = 1, message = ConstraintMessages.MIN)
  private Integer[] customerId;

  @FilterOperation(op = Operation.EQUAL)
  @MinArray(value = 1, message = ConstraintMessages.MIN)
  private Integer[] itemId;

  @FilterOperation(op = Operation.GREATER_THAN_OR_EQUAL, fieldName = "purchaseDate")
  @PastOrPresent(message = ConstraintMessages.PAST_OR_PRESENT)
  private LocalDate purchaseDateBeg;

  @FilterOperation(op = Operation.LESS_THAN_OR_EQUAL, fieldName = "purchaseDate")
  @PastOrPresent(message = ConstraintMessages.PAST_OR_PRESENT)
  private LocalDate purchaseDateEnd;

  @FilterOperation(op = Operation.GREATER_THAN_OR_EQUAL, fieldName = "price")
  @Min(value = 0, message = ConstraintMessages.MIN)
  private BigDecimal priceBeg;

  @FilterOperation(op = Operation.LESS_THAN_OR_EQUAL, fieldName = "price")
  @Min(value = 0, message = ConstraintMessages.MIN)
  private BigDecimal priceEnd;

  @Min(value = 0, message = ConstraintMessages.MIN)
  private Integer page;

  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer size;

  private String[] orderBy;
  private Boolean[] ascOrder;
}
