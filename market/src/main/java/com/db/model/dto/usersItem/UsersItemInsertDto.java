package com.db.model.dto.usersItem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersItemInsertDto {
  @NotNull
  @Min(1)
  private Integer userId;

  @NotNull
  @Min(1)
  private Integer itemId;

  @NotNull
  @Min(1)
  private Integer quantity;
}
