package com.db.model.dto.developer;

import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperUpdateDto {
  private String name;

  private String address;

  @Min(1)
  private Integer countryId;
}
