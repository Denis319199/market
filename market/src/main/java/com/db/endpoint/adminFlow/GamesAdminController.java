package com.db.endpoint.adminFlow;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Game;
import com.db.model.dto.game.GameInsertDto;
import com.db.model.dto.game.GameUpdateDto;
import com.db.service.GamesService;
import com.db.utility.mapper.ModelMapper;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/game")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class GamesAdminController {

  private final GamesService gamesService;
  private final ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation
  Game insertGame(@RequestBody @Valid GameInsertDto gameDto) throws ServiceException {
    try {
      return gamesService.insertGame(modelMapper.map(gameDto, Game.class));
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  Game updateGame(@RequestBody @Valid GameUpdateDto gameDto) throws ServiceException {
    try {
      return gamesService.updateGame(modelMapper.map(gameDto, Game.class));
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  void deleteGame(@RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int id)
      throws ServiceException {
    try {
      gamesService.deleteGame(id);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
