package com.db.model.dto.sellingItem;

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
  @Min(1)
  @NotNull
  private Integer itemId;

  @Min(1)
  @NotNull
  private BigDecimal price;
}
