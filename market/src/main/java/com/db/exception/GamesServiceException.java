package com.db.exception;

public class GamesServiceException extends Exception {

  public static final String GAME_NOT_FOUND = "Game not found";
  public static final String GAME_ALREADY_EXISTS = "Game already exists";
  public static final String GAMES_IMAGE_NOT_FOUND = "Game's image not found";
  public static final String GAMES_IMAGE_ALREADY_EXISTS = "Game's image already exists";

  public GamesServiceException(String message) {
    super(message);
  }
}
