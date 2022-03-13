package com.db.model.dto.developer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperInsertDto {
  @NotNull private String name;

  @NotNull private String address;

  @NotNull
  @Min(1)
  private Integer countryId;
}
