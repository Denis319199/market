package com.db.utility.filter.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterResult<T> {
    private List<T> content;
    private Integer totalElementCount;
    private Integer totalPageCount;
}
