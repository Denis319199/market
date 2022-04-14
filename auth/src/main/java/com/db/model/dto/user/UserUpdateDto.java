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
public class UserUpdateDto {
  @NotNull(groups = AdminGroup.class, message = ConstraintMessages.NOT_NULL)
  @Null(groups = PlainUserGroup.class, message = ConstraintMessages.NULL)
  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer id;

  @Size(min = 5, max = 64, message = ConstraintMessages.SIZE)
  private String username;

  @Size(min = 5, max = 64, message = ConstraintMessages.SIZE)
  private String password;

  @Null(groups = PlainUserGroup.class, message = ConstraintMessages.NULL)
  private Role role;

  @Null(groups = PlainUserGroup.class, message = ConstraintMessages.NULL)
  private Boolean isEnabled;

  @Size(min = 1, max = 64, message = ConstraintMessages.SIZE)
  private String firstName;

  @Size(min = 1, max = 64, message = ConstraintMessages.SIZE)
  private String lastName;

  @Size(min = 1, max = 64, message = ConstraintMessages.SIZE)
  private String patronymic;

  @Min(value = 1_000_000_000L, message = ConstraintMessages.MIN)
  @Max(value = 9_999_999_999L, message = ConstraintMessages.MAX)
  private Long phoneNumber;

  @Min(value = 1, message = ConstraintMessages.MIN)
  private Integer countryId;
}
