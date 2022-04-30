package com.db.model.filter;

import com.db.model.Item;
import com.db.utility.filter.annotation.FilterModel;
import com.db.utility.filter.annotation.FilterOperation;
import com.db.utility.filter.model.Operation;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.annotation.MinArray;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@FilterModel(Item.class)
@Data
@NoArgsConstructor
public class ItemFilter {
  @FilterOperation(op = Operation.LIKE)
  @Size(min = 1, max = 128, message = ConstraintMessages.SIZE)
  private String name;

  @FilterOperation(op = Operation.EQUAL)
  @MinArray(value = 1, message = ConstraintMessages.MIN)
  private Integer[] gameId;

  @Min(value = 0, message = ConstraintMessages.MIN)
  private Integer page;

  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer size;

  private String[] orderBy;
  private Boolean[] ascOrder;
}
