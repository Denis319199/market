package com.db.model.dto.usersItem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersItemUpdateDto {
  @NotNull
  @Min(1)
  private Integer userId;

  @NotNull
  @Min(1)
  private Integer itemId;

  @Min(1)
  private Integer quantity;
}
