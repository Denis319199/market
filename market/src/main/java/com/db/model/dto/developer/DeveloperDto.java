package com.db.model.dto.developer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDto {
    private Integer id;

    private String name;

    private String address;

    private Integer countryId;
}
