package com.db.model.dto.user;

import com.db.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendedDto {

  private Integer id;

  private String username;

  private Role role;

  private String firstName;

  private String lastName;

  private String patronymic;

  private Long phoneNumber;

  private Integer countryId;

  private Boolean isImagePresented;
}
