package com.db.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeveloperResponseDto {
  @Data
  @NoArgsConstructor
  public static class DeveloperDto {
    private int id;
    private String name;
  }

  private String next;
  private DeveloperDto[] results;
}
