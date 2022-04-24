package com.db.service.impl;

import com.db.exception.GamesServiceException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GamesServiceImpl implements GamesService {
  private final GamesRepo gamesRepo;
  private final GamesImagesRepo gamesImagesRepo;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public Game findGameById(int id) throws GamesServiceException {
    Optional<Game> game = gamesRepo.findById(id);

    if (game.isEmpty()) {
      throw new GamesServiceException(GamesServiceException.GAME_NOT_FOUND);
    }

    return game.get();
  }

  @Transactional(readOnly = true)
  public List<Game> getAllGames(int page, int size) {
    return gamesRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Game insertGame(Game game) throws GamesServiceException {
    try {
      return gamesRepo.save(game);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

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

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteGame(int id) throws GamesServiceException {
    try {
      gamesRepo.deleteById(id);
    } catch (EmptyResultDataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public GamesImage getGamesImage(int gameId, int num) throws GamesServiceException {
    Optional<GamesImage> image = gamesImagesRepo.findImageByGameIdWithOffset(gameId, num);
    if (image.isEmpty()) {
      throw new GamesServiceException(GamesServiceException.GAMES_IMAGE_NOT_FOUND);
    }

    return image.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void insertGamesImage(GamesImage image) throws GamesServiceException {
    try {
      gamesImagesRepo.save(image);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void updateGamesImage(GamesImage image) throws GamesServiceException {
    if (!gamesImagesRepo.existsById(image.getId())) {
      throw new GamesServiceException(GamesServiceException.GAMES_IMAGE_NOT_FOUND);
    }

    try {
      gamesImagesRepo.save(image);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteGamesImage(int id) throws GamesServiceException {
    try {
      gamesImagesRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new GamesServiceException(GamesServiceException.GAME_NOT_FOUND);
    }
  }
}
