package com.db.model.dto.purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseUpdateDto {
  @NotNull private Integer id;

  @Min(1)
  private Integer sellerId;

  @Min(1)
  private Integer customerId;

  @Min(1)
  private Integer itemId;

  private LocalDate purchaseDate;

  @Min(1)
  private BigDecimal price;
}
