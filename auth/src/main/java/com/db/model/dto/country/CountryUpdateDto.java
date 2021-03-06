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
public class CountryUpdateDto {
    @NotNull
    @Min(1)
    private Integer id;

    @Size(min = 1, max = 128)
    private String name;

    private Integer phoneCode;
}
