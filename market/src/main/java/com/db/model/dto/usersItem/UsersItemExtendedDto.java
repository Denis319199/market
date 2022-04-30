package com.db.model.dto.usersItem;

import com.db.model.Item;
import com.db.utility.Recursive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsersItemExtendedDto {
  private Integer userId;

  @Recursive private Item item;

  private Integer quantity;
}
