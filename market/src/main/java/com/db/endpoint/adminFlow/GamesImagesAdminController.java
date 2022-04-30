package com.db.endpoint.adminFlow;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.model.GamesImage;
import com.db.service.GamesService;
import com.db.utility.validation.ConstraintMessages;
import com.db.utility.validation.annotation.Image;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/game/image")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Validated
public class GamesImagesAdminController {
  private final GamesService gamesService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation
  public void insertGamesImage(
      @RequestParam @Image MultipartFile image, @RequestParam @Min(1) int gameId)
      throws IOException, ServiceException {
    try {
      gamesService.insertGamesImage(new GamesImage(null, gameId, image.getBytes()));
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation
  public void deleteGamesImage(
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int gameId,
      @RequestParam @Min(value = 1, message = ConstraintMessages.MIN) int num)
      throws ServiceException {
    try {
      gamesService.deleteGamesImage(gameId, num);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
