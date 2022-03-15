package com.db.model.dto.item;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInsertDto {
    @NotNull
    private String name;

    @NotNull
    private Integer gameId;
}
