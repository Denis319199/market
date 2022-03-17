package com.db.service;

import com.db.exception.UsersServiceException;
import com.db.model.User;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UsersService usersService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User user = usersService.findUserByUsername(username);
      return org.springframework.security.core.userdetails.User.builder()
          .username(user.getUsername())
          .password(user.getPassword())
          .disabled(!user.getIsEnabled())
          .authorities(user.getRole().name())
          .build();
    } catch (UsersServiceException ex) {
      throw new UsernameNotFoundException(ex.getMessage());
    }
  }
}
