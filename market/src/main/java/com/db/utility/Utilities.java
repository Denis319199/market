package com.db.utility;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utilities {
  public static <T> void merge(T target, T data) {
    Map<String, Method> gettersAndSetters =
        Arrays.stream(target.getClass().getDeclaredMethods())
            .filter(method -> method.getName().contains("set") || method.getName().contains("get"))
            .collect(Collectors.toMap(Method::getName, Function.identity()));

    gettersAndSetters
        .values()
        .forEach(
            method -> {
              if (method.getName().contains("get")) {
                try {
                  if (Objects.isNull(method.invoke(target))) {
                    gettersAndSetters
                        .get(method.getName().replace("get", "set"))
                        .invoke(target, method.invoke(data));
                  }
                } catch (Exception ignored) {

                }
              }
            });
  }
}
