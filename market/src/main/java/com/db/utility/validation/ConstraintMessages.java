package com.db.utility.validation;

public class ConstraintMessages {
    public static final String NOT_NULL = "value must be provided";
    public static final String SIZE = "value has wrong size, either too short or too long";
    public static final String NULL = "value must not be provided, it is necessary";
    public static final String MIN = "value is too small";
    public static final String MAX = "value is too big";
    public static final String PAST_OR_PRESENT = "time must be within the past or be present";
}
