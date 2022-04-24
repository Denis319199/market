package com.db.utility;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utilities {
  public static String translateFromCamelToSnakeCase(String name) {
    StringBuilder builder = new StringBuilder(name);
    for (int i = 0; i < builder.length(); ++i) {
      char symbol = builder.charAt(i);
      if (Character.isUpperCase(symbol)) {
        builder.setCharAt(i, Character.toLowerCase(symbol));
        builder.insert(i, '_');
      }
    }

    return builder.toString();
  }
}
