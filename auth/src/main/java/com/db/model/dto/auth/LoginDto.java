package com.db.model.dto.auth;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
  @NotNull(message = "must not be null")
  private String username;

  @NotNull(message = "must not be null")
  private String password;
}
