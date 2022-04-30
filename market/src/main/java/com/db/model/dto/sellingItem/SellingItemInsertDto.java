package com.db.model.dto.sellingItem;

import com.db.utility.validation.ConstraintMessages;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellingItemInsertDto {
  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer itemId;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 0, message = ConstraintMessages.MIN)
  private BigDecimal price;
}
