package com.db.model.dto.item;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateDto {
  @NotNull
  private Integer id;

  private String name;

  private Integer gameId;
}
