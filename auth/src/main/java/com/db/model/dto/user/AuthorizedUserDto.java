package com.db.model.dto.user;

import com.db.model.dto.auth.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizedUserDto {
  UserDto user;

  TokenDto token;
}
