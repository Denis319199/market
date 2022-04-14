package com.db.model.dto.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {
  private Integer id;

  private String name;

  private Integer phoneCode;
}
