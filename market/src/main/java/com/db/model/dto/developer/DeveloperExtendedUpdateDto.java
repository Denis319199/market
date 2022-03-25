package com.db.model.dto.developer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperExtendedUpdateDto {
  @NotNull
  @Min(1)
  private Integer id;

  private String name;

  private String address;

  @Min(1)
  private Integer countryId;
}
