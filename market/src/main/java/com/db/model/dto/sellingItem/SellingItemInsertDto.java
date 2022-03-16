package com.db.model.dto.sellingItem;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellingItemInsertDto {
  @Min(1)
  private Integer itemId;

  @Min(1)
  private Integer sellerId;

  @Min(1)
  private BigDecimal price;
}
