package com.db.model.filter;

import com.db.model.SellingItem;
import com.db.utility.filter.annotation.FilterInnerJoin;
import com.db.utility.filter.annotation.FilterModel;
import com.db.utility.filter.annotation.FilterOperation;
import com.db.utility.filter.model.Operation;
import com.db.utility.validation.ConstraintMessages;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@FilterModel(SellingItem.class)
@Data
@NoArgsConstructor
public class SellingItemFilter {
  @FilterOperation(op = Operation.EQUAL, flag = "isOwn")
  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer sellerId;

  @FilterInnerJoin(lhs = "itemId", rhs = "id")
  private ItemFilter item;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  private Boolean isOwn;

  @Min(value = 0, message = ConstraintMessages.MIN)
  private Integer page;

  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer size;

  private String[] orderBy;

  private Boolean[] ascOrder;
}
