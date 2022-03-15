package com.db.model.dto.game;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInsertDto {
  @NotNull private String name;

  @NotNull private String description;

  @NotNull
  @Min(1)
  private Integer developerId;

  @NotNull private LocalDate releaseDate;

  @NotNull
  @Min(0)
  private BigDecimal price;
}
