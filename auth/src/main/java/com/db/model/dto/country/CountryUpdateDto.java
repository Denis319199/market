package com.db.model.dto.country;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryUpdateDto {
    @NotNull
    @Min(1)
    private Integer id;

    private String name;

    private Integer phoneCode;
}
