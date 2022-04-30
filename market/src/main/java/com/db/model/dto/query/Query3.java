package com.db.model.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query3 {
  private Integer countryId;
  private String countryName;
  private Integer id;
  private String username;
  private Integer num;
}
