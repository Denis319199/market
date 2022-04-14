package com.db.model.dto.auth;

import com.db.utility.validation.ConstraintMessages;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
  @NotNull(message = ConstraintMessages.NOT_NULL)
  private String username;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  private String password;
}
