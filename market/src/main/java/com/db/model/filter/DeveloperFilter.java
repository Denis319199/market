package com.db.model.filter;

import com.db.model.Developer;
import com.db.utility.sql.filter.annotation.FilterModel;
import com.db.utility.sql.filter.annotation.FilterOperation;
import com.db.utility.sql.filter.model.Operation;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.annotation.MinArray;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@FilterModel(Developer.class)
@Data
@NoArgsConstructor
public class DeveloperFilter {
  @FilterOperation(op = Operation.LIKE)
  @Size(min = 1, max = 128, message = ConstraintMessages.SIZE)
  private String name;

  @FilterOperation(op = Operation.LIKE)
  @Size(min = 1, max = 256, message = ConstraintMessages.SIZE)
  private String address;

  @FilterOperation(op = Operation.EQUAL)
  @MinArray(value = 1, message = ConstraintMessages.MIN)
  private Integer[] countryId;

  @Min(value = 0, message = ConstraintMessages.MIN)
  private Integer page;

  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer size;

  private String[] orderBy;

  private Boolean[] ascOrder;
}
