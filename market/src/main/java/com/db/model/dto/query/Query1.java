package com.db.model.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Query1 {
  private Integer id;
  private String name;
  private Integer numberOfItems;
}
