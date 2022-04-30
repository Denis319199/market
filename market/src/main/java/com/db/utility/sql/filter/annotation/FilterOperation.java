package com.db.utility.sql.filter.annotation;

import com.db.utility.sql.filter.model.Operation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
public @interface FilterOperation {
    Operation op();

    String fieldName() default "";

    String flag() default "";
}
