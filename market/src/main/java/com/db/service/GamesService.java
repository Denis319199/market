package com.db.service;

import com.db.exception.GamesServiceException;
import com.db.model.Game;
import com.db.model.GamesImage;
import java.util.List;

public interface GamesService {
  Game findGameById(int id) throws GamesServiceException;

  List<Game> getAllGames(int page, int size);

  Game insertGame(Game game) throws GamesServiceException;

  Game updateGame(Game game) throws GamesServiceException;

  void deleteGame(int id) throws GamesServiceException;

  GamesImage getGamesImage(int gameId, int num) throws GamesServiceException;

  void insertGamesImage(GamesImage image) throws GamesServiceException;

  void updateGamesImage(GamesImage image) throws GamesServiceException;

  void deleteGamesImage(int id) throws GamesServiceException;
}
