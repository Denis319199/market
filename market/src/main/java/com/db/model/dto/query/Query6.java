package com.db.model.dto.query;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query6 {
    private Integer id;
    private String name;
    private BigDecimal price;
}
