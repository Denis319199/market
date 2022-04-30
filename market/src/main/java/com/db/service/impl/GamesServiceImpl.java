package com.db.service.impl;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Game;
import com.db.model.GamesImage;
import com.db.repo.GamesImagesRepo;
import com.db.repo.GamesRepo;
import com.db.service.GamesService;
import com.db.utility.Utilities;
import com.db.utility.mapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GamesServiceImpl implements GamesService {
  private final GamesRepo gamesRepo;
  private final GamesImagesRepo gamesImagesRepo;
  private final ModelMapper modelMapper;

  @Override
  @Transactional(readOnly = true)
  public Game findGameById(int id) throws GamesServiceException {
    Optional<Game> game = gamesRepo.findById(id);

    if (game.isEmpty()) {
      throw new GamesServiceException(GamesServiceException.GAME_NOT_FOUND);
    }

    return game.get();
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Game insertGame(Game game) throws GamesServiceException {
    try {
      return gamesRepo.save(game);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Game updateGame(Game game) throws GamesServiceException {
    Game old = findGameById(game.getId());
    modelMapper.merge(game, old);

    try {
      return gamesRepo.save(game);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteGame(int id) throws GamesServiceException {
    try {
      gamesRepo.deleteById(id);
    } catch (EmptyResultDataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public GamesImage getGamesImage(int gameId, int num) throws GamesServiceException {
    Optional<GamesImage> image = gamesImagesRepo.findImageByGameIdWithOffset(gameId, num);
    if (image.isEmpty()) {
      throw new GamesServiceException(GamesServiceException.GAMES_IMAGE_NOT_FOUND);
    }

    return image.get();
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void insertGamesImage(GamesImage image) throws GamesServiceException, ServiceException {
    try {
      gamesImagesRepo.save(image);
      changeTotalImageCounter(image.getGameId(), true);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void deleteGamesImage(int gameId, int num) throws GamesServiceException, ServiceException {
    try {
      gamesImagesRepo.deleteGamesImageByGameIdAndNumber(gameId, num);
      changeTotalImageCounter(gameId, false);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(GamesServiceException.GAME_NOT_FOUND);
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void changeTotalImageCounter(int gameId, boolean isInsert) throws ServiceException {
    try {
      Game game = findGameById(gameId);

      if (isInsert) {
        game.setTotalImageCount(game.getTotalImageCount() + 1);
      } else {
        game.setTotalImageCount(game.getTotalImageCount() - 1);
      }

      gamesRepo.save(game);
    } catch (GamesServiceException | DataAccessException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
