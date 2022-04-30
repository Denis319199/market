package com.db.model.dto.sellingItem;

import com.db.model.Item;
import com.db.utility.validation.annotation.Recursive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SellingItemExtendedDto {
  private Integer id;

  @Recursive private Item item;

  private Integer sellerId;

  private BigDecimal price;
}
