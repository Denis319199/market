package com.db.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GamesResponseDto {
  @Data
  @NoArgsConstructor
  public static class GameDto {
    private String name;
    private LocalDate released;

    @JsonProperty("background_image")
    private String backgroundImage;
  }

  private String next;
  private GameDto[] results;
}
