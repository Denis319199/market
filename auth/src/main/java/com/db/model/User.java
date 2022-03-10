package com.db.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  @Enumerated
  private Role role;

  @Column(name = "is_enabled")
  private Boolean isEnabled;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "patronymic")
  private String patronymic;

  @Column(name = "phone_number")
  private Long phoneNumber;

  @Column(name = "country_id")
  private Integer countryId;

  public void mergeWith(User other) {
    if (Objects.isNull(id)) {
      id = other.id;
    }
    if (Objects.isNull(username)) {
      username = other.username;
    }
    if (Objects.isNull(password)) {
      password = other.password;
    }
    if (Objects.isNull(role)) {
      role = other.role;
    }
    if (Objects.isNull(isEnabled)) {
      isEnabled = other.isEnabled;
    }
    if (Objects.isNull(firstName)) {
      firstName = other.firstName;
    }
    if (Objects.isNull(lastName)) {
      lastName = other.lastName;
    }
    if (Objects.isNull(patronymic)) {
      patronymic = other.patronymic;
    }
    if (Objects.isNull(phoneNumber)) {
      phoneNumber = other.phoneNumber;
    }
    if (Objects.isNull(countryId)) {
      countryId = other.countryId;
    }
  }
}
