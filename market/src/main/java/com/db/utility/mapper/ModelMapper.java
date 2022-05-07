package com.db.utility.mapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {
  public <T> T map(Object source, Class<T> targetClass) {
    try {
      Map<String, Method> targetSetters =
          Arrays.stream(targetClass.getDeclaredMethods())
              .filter(method -> method.getName().startsWith("set"))
              .collect(Collectors.toMap(Method::getName, Function.identity()));

      T result = targetClass.getConstructor().newInstance();

      Arrays.stream(source.getClass().getDeclaredMethods())
          .filter(method -> method.getName().startsWith("get"))
          .forEach(
              method -> {
                try {
                  targetSetters
                      .get(method.getName().replaceFirst("get", "set"))
                      .invoke(result, method.invoke(source));
                } catch (Exception ignore) {
                }
              });

      return result;
    } catch (Exception ex) {
      return null;
    }
  }

  public <T> void merge(T target, T source) {
    Map<String, Method> setters = new HashMap<>();
    Map<String, Method> getters = new HashMap<>();

    for (Method method : target.getClass().getDeclaredMethods()) {
      String methodName = method.getName();
      if (methodName.startsWith("set")) {
        setters.put(methodName, method);
      } else if (methodName.startsWith("get")) {
        getters.put(methodName, method);
      }
    }

    getters.forEach(
        (methodName, method) -> {
          try {
            Object obj = method.invoke(source);
            if (obj != null && method.invoke(target) == null) {
              setters.get(methodName.replaceFirst("get", "set")).invoke(target, obj);
            }
          } catch (Exception ignore) {
          }
        });
  }
}
