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
public class GameUpdateDto {
  @NotNull
  @Min(1)
  private Integer id;

  private String name;

  private String description;

  @Min(1)
  private Integer developerId;

  private LocalDate releaseDate;

  @Min(0)
  private BigDecimal price;
}
