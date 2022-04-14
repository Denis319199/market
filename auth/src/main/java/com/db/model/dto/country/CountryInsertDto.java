package com.db.model.dto.country;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryInsertDto {
    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    @NotNull
    private Integer phoneCode;
}
