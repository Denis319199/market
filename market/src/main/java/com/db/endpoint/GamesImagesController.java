package com.db.endpoint;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.service.GamesService;
import com.db.utility.validation.ConstraintMessages;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/image")
@RequiredArgsConstructor
@Validated
public class GamesImagesController {
  private final GamesService gamesService;

  @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public byte[] getGamesImage(
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int gameId,
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int num)
      throws ServiceException {
    try {
      return gamesService.getGamesImage(gameId, num).getImage();
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
