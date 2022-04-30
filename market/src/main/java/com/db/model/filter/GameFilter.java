package com.db.model.filter;

import com.db.model.Game;
import com.db.utility.sql.filter.annotation.FilterModel;
import com.db.utility.sql.filter.annotation.FilterOperation;
import com.db.utility.sql.filter.model.Operation;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.annotation.MinArray;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@FilterModel(Game.class)
@Data
@NoArgsConstructor
public class GameFilter {
  @FilterOperation(op = Operation.LIKE)
  @Size(min = 1, max = 128, message = ConstraintMessages.SIZE)
  private String name;

  @FilterOperation(op = Operation.EQUAL)
  @MinArray(value = 1, message = ConstraintMessages.MIN)
  private Integer[] developerId;

  @FilterOperation(op = Operation.GREATER_THAN_OR_EQUAL, fieldName = "releaseDate")
  @PastOrPresent(message = ConstraintMessages.PAST_OR_PRESENT)
  private LocalDate releaseDateBeg;

  @FilterOperation(op = Operation.LESS_THAN_OR_EQUAL, fieldName = "releaseDate")
  @PastOrPresent(message = ConstraintMessages.PAST_OR_PRESENT)
  private LocalDate releaseDateEnd;

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
