package com.db.endpoint;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Game;
import com.db.model.filter.GameFilter;
import com.db.service.GamesService;
import com.db.utility.sql.filter.SqlFilter;
import com.db.utility.sql.filter.model.FilterResult;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
@Validated
public class GamesController {
  private final GamesService gamesService;
  private final SqlFilter sqlFilter;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public FilterResult<Game> getGames(@RequestBody @Valid GameFilter query) throws ServiceException {
    return sqlFilter.doFilter(query, Game.class);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public Game getGame(@PathVariable @Min(value = 1, message = ConstraintMessages.MIN) int id)
      throws ServiceException {
    try {
      return gamesService.findGameById(id);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
