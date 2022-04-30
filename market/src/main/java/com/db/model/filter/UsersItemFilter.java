package com.db.model.filter;

import com.db.model.UsersItem;
import com.db.utility.sql.filter.annotation.FilterInnerJoin;
import com.db.utility.sql.filter.annotation.FilterModel;
import com.db.utility.sql.filter.annotation.FilterOperation;
import com.db.utility.sql.filter.model.Operation;
import com.db.utility.validation.ConstraintMessages;
import javax.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@FilterModel(UsersItem.class)
@Data
@NoArgsConstructor
public class UsersItemFilter {
    @FilterOperation(op = Operation.EQUAL)
    @Min(value = 1, message = ConstraintMessages.MIN)
    private Integer userId;

    @FilterInnerJoin(lhs = "itemId", rhs = "id")
    private ItemFilter item;

    @Min(value = 0, message = ConstraintMessages.MIN)
    private Integer page;

    @Min(value = 1, message = ConstraintMessages.MIN)
    private Integer size;

    private String[] orderBy;
    private Boolean[] ascOrder;
}
