package com.db.model.dto.user;

import com.db.model.Role;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendedInsertDto {
  @NotNull(message = "must not be null")
  private String username;

  @NotNull(message = "must not be null")
  private String password;

  @NotNull(message = "must not be null")
  private Role role;

  @NotNull(message = "must not be null")
  private Boolean isEnabled;

  @NotNull(message = "must not be null")
  private String firstName;

  @NotNull(message = "must not be null")
  private String lastName;
  
  private String patronymic;

  @NotNull(message = "must not be null")
  @Min(value = 1_000_000_000L, message = "must be 10 digits, starting from 1000000000")
  @Max(value = 9_999_999_999L, message = "must be 10 digits, starting from 1000000000")
  private Long phoneNumber;

  @NotNull(message = "must not be null")
  @Min(1)
  private Integer countryId;
}
