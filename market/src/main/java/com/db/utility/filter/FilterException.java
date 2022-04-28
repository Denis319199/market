package com.db.utility.filter;

public class FilterException extends Exception {

  public static final String CANNOT_GET_ACCESS_TO_FIELD = "Filter cannot access to some field";
  public static final String THERE_IS_NO_SUCH_ATTRIBUTE = "There is no such attribute";
  public static final String INVALID_CONDITION_FOR_INNER_JOIN = "Invalid condition for inner join";
  public static final String DIFFERENT_SORTING_PARAMETER_LENGTHS =
      "'orderBy' has no the same size as 'ascOrder'";
  public static final String NO_ID_FOR_ORDER = "Model must have field annotated as 'Id'";
  public static final String INVALID_PAGE_NUMBER_OR_PAGE_SIZE =
      "Invalid value for page number or page size";

  private final String additionalInfo;

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public FilterException(String message) {
    super(message);
    additionalInfo = null;
  }

  public FilterException(String message, String additionalInfo) {
    super(message);
    this.additionalInfo = additionalInfo;
  }

  public String getFullMessage() {
    return additionalInfo == null ? getMessage() : getMessage() + " - " + getAdditionalInfo();
  }
}
