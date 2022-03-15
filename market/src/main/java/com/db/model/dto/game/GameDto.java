package com.db.model.dto.game;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
  private Integer id;

  private String name;

  private String description;

  private Integer developerId;

  private LocalDate releaseDate;

  private BigDecimal price;
}
