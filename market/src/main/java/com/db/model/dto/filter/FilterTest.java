package com.db.model.dto.filter;

import com.db.model.Developer;
import com.db.utility.filter.annotation.FilterModel;
import com.db.utility.filter.annotation.FilterOperation;
import com.db.utility.filter.Operation;
import lombok.Builder;
import lombok.Data;

@FilterModel(Developer.class)
@Data
@Builder
public class FilterTest {
    @FilterOperation(op = Operation.EQUAL, flag = "notEqual")
    String name;

    Boolean notEqual;

    String orderBy;

    int page;

    int size;
}
