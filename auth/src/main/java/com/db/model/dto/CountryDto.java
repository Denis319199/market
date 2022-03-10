package com.db.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {
    @NotNull
    @Min(1)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private Integer phoneCode;
}
