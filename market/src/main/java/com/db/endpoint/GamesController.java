package com.db.endpoint;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Game;
import com.db.model.dto.game.GameDto;
import com.db.model.dto.game.GameInsertDto;
import com.db.model.dto.game.GameUpdateDto;
import com.db.service.GamesService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Validated
public class GamesController {
  private final GamesService gamesService;
  private final ModelMapper modelMapper;

  @GetMapping
  List<GameDto> getAllGames(
      @RequestParam @Min(value = 0, message = "starts from 0") int page,
      @RequestParam @Min(value = 1, message = "must be more than 0") int size) {
    return gamesService.getAllGames(page, size).stream()
        .map(game -> modelMapper.map(game, GameDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  GameDto getGame(@PathVariable @Min(value = 1, message = "must be more than 0") int id)
      throws ServiceException {
    try {
      return modelMapper.map(gamesService.findGameById(id), GameDto.class);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  GameDto insertGame(@RequestBody @Valid GameInsertDto gameDto) throws ServiceException {
    try {
      Game game = gamesService.insertGame(modelMapper.map(gameDto, Game.class));
      return modelMapper.map(game, GameDto.class);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  GameDto updateGame(@RequestBody @Valid GameUpdateDto gameDto) throws ServiceException {
    try {
      Game game = gamesService.updateGame(modelMapper.map(gameDto, Game.class));
      return modelMapper.map(game, GameDto.class);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  void deleteGame(@RequestParam @Min(value = 1, message = "must be more than 0") int id) throws ServiceException {
    try {
      gamesService.deleteGame(id);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
