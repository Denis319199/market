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
public class UserExtendedUpdateDto {
    @NotNull(message = "must not be null")
    @Min(1)
    private Integer id;

    private String username;

    private String password;

    private Role role;

    private Boolean isEnabled;

    private String firstName;

    private String lastName;

    private String patronymic;

    @Min(value = 1_000_000_000L, message = "must be 10 digits, starting from 1000000000")
    @Max(value = 9_999_999_999L, message = "must be 10 digits, starting from 1000000000")
    private Long phoneNumber;

    @Min(1)
    private Integer countryId;
}
