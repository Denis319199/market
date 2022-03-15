package com.db.endpoint;

import com.db.exception.GamesServiceException;
import com.db.exception.ServiceException;
import com.db.model.GamesImage;
import com.db.service.GamesService;
import java.io.IOException;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/games/images")
@RequiredArgsConstructor
@Validated
public class GamesImagesController {
  private final GamesService gamesService;

  @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
  byte[] getGamesImage(@RequestParam @Min(1) int gameId, @RequestParam @Min(0) int num)
      throws ServiceException {
    try {
      return gamesService.getGamesImage(gameId, num).getImage();
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  void insertGamesImage(@RequestParam MultipartFile image, @RequestParam @Min(1) int gameId)
      throws IOException, ServiceException {
    try {
      gamesService.insertGamesImage(new GamesImage(null, gameId, image.getBytes()));
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping
  void updateGamesImage(@RequestParam MultipartFile image, @RequestParam @Min(1) int gameId)
      throws IOException, ServiceException {
    try {
      gamesService.updateGamesImage(new GamesImage(null, gameId, image.getBytes()));
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  void deleteGamesImage(@RequestParam @Min(1) int id) throws ServiceException {
    try {
      gamesService.deleteGamesImage(id);
    } catch (GamesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
