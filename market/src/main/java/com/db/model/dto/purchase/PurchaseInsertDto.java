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
public class PurchaseInsertDto {
    @Min(1)
    @NotNull
    private Integer sellerId;

    @Min(1)
    @NotNull
    private Integer customerId;

    @Min(1)
    @NotNull
    private Integer itemId;

    @NotNull
    private LocalDate purchaseDate;

    @Min(1)
    @NotNull
    private BigDecimal price;
}
