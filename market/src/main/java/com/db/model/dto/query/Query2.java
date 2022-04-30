package com.db.model.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query2 {
  private Integer id;
  private String username;
  private Integer quantity;
}
