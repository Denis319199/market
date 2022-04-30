package com.db.model.dto.game;

import com.db.utility.validation.ConstraintMessages;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInsertDto {
  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Size(min = 1, max = 128, message = ConstraintMessages.SIZE)
  private String name;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Size(min = 1, max = 1024, message = ConstraintMessages.SIZE)
  private String description;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer developerId;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @PastOrPresent(message = ConstraintMessages.PAST_OR_PRESENT)
  private LocalDate releaseDate;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 0, message = ConstraintMessages.MIN)
  private BigDecimal price;
}
