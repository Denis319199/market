package com.db.model.dto.user;

import com.db.model.Role;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.group.AdminGroup;
import com.db.utility.validation.group.PlainUserGroup;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInsertDto {
  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Size(min = 5, max = 64, message = ConstraintMessages.SIZE)
  private String username;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Size(min = 5, max = 64, message = ConstraintMessages.SIZE)
  private String password;

  @NotNull(groups = AdminGroup.class, message = ConstraintMessages.NOT_NULL)
  @Null(groups = PlainUserGroup.class, message = ConstraintMessages.NULL)
  private Role role;

  @NotNull(groups = AdminGroup.class, message = ConstraintMessages.NOT_NULL)
  @Null(groups = PlainUserGroup.class, message = ConstraintMessages.NULL)
  private Boolean isEnabled;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Size(min = 1, max = 64, message = ConstraintMessages.SIZE)
  private String firstName;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Size(min = 1, max = 64, message = ConstraintMessages.SIZE)
  private String lastName;

  @Size(min = 1, max = 64, message = ConstraintMessages.SIZE)
  private String patronymic;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 1_000_000_000L, message = ConstraintMessages.MIN)
  @Max(value = 9_999_999_999L, message = ConstraintMessages.MAX)
  private Long phoneNumber;

  @NotNull(message = ConstraintMessages.NOT_NULL)
  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer countryId;
}
