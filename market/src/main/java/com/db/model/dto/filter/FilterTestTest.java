package com.db.model.dto.filter;

import com.db.model.Game;
import com.db.utility.filter.annotation.FilterInnerJoin;
import com.db.utility.filter.annotation.FilterModel;
import com.db.utility.filter.annotation.FilterOperation;
import com.db.utility.filter.Operation;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@FilterModel(Game.class)
@Builder
@Data
public class FilterTestTest {
    @FilterInnerJoin(lhs = "developerId", rhs = "id")
    private FilterTest filterTest;

    @FilterOperation(op = Operation.LESS_THAN_OR_EQUAL)
    private LocalDate releaseDate;

    private String orderBy;

    private Boolean ascOrder;

    private int page;

    private int size;
}
