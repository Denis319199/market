package com.db.model.dto.developer;

import com.db.utility.validation.ConstraintMessages;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperUpdateDto {
  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer id;

  @Size(min = 1, max = 128, message = ConstraintMessages.SIZE)
  private String name;

  @Size(min = 1, max = 256, message = ConstraintMessages.SIZE)
  private String address;

  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer countryId;
}
