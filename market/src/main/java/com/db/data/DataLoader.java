package com.db.data;

import com.db.exception.DevelopersServiceException;
import com.db.exception.GamesServiceException;
import com.db.exception.ItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.Developer;
import com.db.model.Game;
import com.db.model.GamesImage;
import com.db.model.ItemsImage;
import com.db.service.DevelopersService;
import com.db.service.GamesService;
import com.db.service.ItemsService;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("load-data")
@RequiredArgsConstructor
@Slf4j
public class DataLoader {
  private static final String DEVELOPERS_URL =
      "https://api.rawg.io/api/developers?key=99e5adaed8ca4668859d5e1c99e34937";
  private static final String GAMES_URL =
      "https://api.rawg.io/api/games?key=99e5adaed8ca4668859d5e1c99e34937";
  private static final String ITEMS_IMAGES_URL = "https://picsum.photos/500/500";

  private static final int REQ_DEVELOPERS_COUNT = 50;
  private static final int REQ_GAMES_COUNT = 500;
  private static final int REQ_ITEMS_IMAGES_COUNT = 1000;

  private final RestTemplate restTemplate = new RestTemplate();
  private final DevelopersService developersService;
  private final GamesService gamesService;
  private final ItemsService itemsService;

  @PostConstruct
  void load() {
    if (isLoadRequired()) {
      loadDevelopers();
      loadGames();
      loadItemsImages();
    }
  }

  private boolean isLoadRequired() {
    try {
      gamesService.getGamesImage(1, 1);
      itemsService.getItemsImage(1);
      log.info("Data loading is not required");
      return false;
    } catch (GamesServiceException | ItemsServiceException ex) {
      return true;
    }
  }

  private void loadDevelopers() {
    log.info("Developers loading has been started");
    log.info("Developers loading: 0%");

    int curDevelopersCount = 0;

    String nextUrl = DEVELOPERS_URL;
    while (true) {
      ResponseEntity<DeveloperResponseDto> response =
          restTemplate.getForEntity(nextUrl, DeveloperResponseDto.class);
      DeveloperResponseDto responseBody = response.getBody();

      if (!response.getStatusCode().equals(HttpStatus.OK) || responseBody == null) {
        log.error("Couldn't get developers, loading has been terminated");
        return;
      }

      for (DeveloperResponseDto.DeveloperDto developerDto : responseBody.getResults()) {
        Developer developer = new Developer();
        developer.setId(curDevelopersCount + 1);
        developer.setName(developerDto.getName());

        try {
          Developer res = developersService.updateDeveloper(developer);
        } catch (DevelopersServiceException ex) {
          log.error("Couldn't get developers, loading has been terminated");
          return;
        }

        if (++curDevelopersCount == REQ_DEVELOPERS_COUNT) {
          log.info("Developers loading: 100%");
          return;
        } else if (curDevelopersCount % (REQ_DEVELOPERS_COUNT / 10) == 0) {
          int percent = (curDevelopersCount / (REQ_DEVELOPERS_COUNT / 10) * 10);
          log.info("Developers loading: " + percent + '%');
        }

        nextUrl = responseBody.getNext();
      }
    }
  }

  private void loadGames() {
    log.info("Games loading has been started");
    log.info("Games loading: 0%");

    String nextUrl = GAMES_URL;
    int curGamesCount = 0;

    while (true) {
      ResponseEntity<GamesResponseDto> response =
          restTemplate.getForEntity(nextUrl, GamesResponseDto.class);
      GamesResponseDto responseBody = response.getBody();

      if (!response.getStatusCode().equals(HttpStatus.OK) || responseBody == null) {
        log.error("Couldn't get games, loading has been terminated");
        return;
      }

      for (GamesResponseDto.GameDto gameDto : responseBody.getResults()) {
        Game game = new Game();
        game.setId(curGamesCount + 1);
        game.setName(gameDto.getName());
        game.setReleaseDate(gameDto.getReleased());

        try {
          gamesService.updateGame(game);
        } catch (GamesServiceException ex) {
          log.error("Couldn't get games, loading has been terminated");
          return;
        }

        GamesImage gamesImage = new GamesImage();
        ResponseEntity<byte[]> imageResponse =
            restTemplate.getForEntity(gameDto.getBackgroundImage(), byte[].class);
        byte[] responseImageBody = imageResponse.getBody();

        if (!imageResponse.getStatusCode().equals(HttpStatus.OK) || responseImageBody == null) {
          log.error("Couldn't get games, loading has been terminated");
          return;
        }

        gamesImage.setGameId(curGamesCount + 1);
        gamesImage.setImage(responseImageBody);

        try {
          gamesService.insertGamesImage(gamesImage);
        } catch (GamesServiceException | ServiceException ex) {
          log.error("Couldn't get games, loading has been terminated");
          return;
        }

        if (++curGamesCount == REQ_GAMES_COUNT) {
          log.info("Games loading: 100%");
          return;
        } else if (curGamesCount % (REQ_GAMES_COUNT / 10) == 0) {
          int percent = (curGamesCount / (REQ_GAMES_COUNT / 10) * 10);
          log.info("Games loading: " + percent + '%');
        }

        nextUrl = responseBody.getNext();
      }
    }
  }

  private void loadItemsImages() {
    log.info("Items images loading has been started");

    for (int curItemsImagesCount = 0;
        curItemsImagesCount < REQ_ITEMS_IMAGES_COUNT;
        ++curItemsImagesCount) {
      ResponseEntity<byte[]> response = restTemplate.getForEntity(ITEMS_IMAGES_URL, byte[].class);
      byte[] responseBody = response.getBody();

      if (!response.getStatusCode().equals(HttpStatus.OK) || responseBody == null) {
        log.error("Couldn't get items images, loading has been terminated");
        return;
      }

      ItemsImage itemsImage = new ItemsImage();
      itemsImage.setItemId(curItemsImagesCount + 1);
      itemsImage.setImage(responseBody);

      try{
         itemsService.putItemsImage(itemsImage);
      } catch (ItemsServiceException ex) {
        log.error("Couldn't get items images, loading has been terminated");
        return;
      }

      if (curItemsImagesCount % (REQ_ITEMS_IMAGES_COUNT / 10) == 0) {
        int percent = (curItemsImagesCount / (REQ_ITEMS_IMAGES_COUNT / 10) * 10);
        log.info("Items images loading: " + percent + '%');
      }
    }

    log.info("Items images loading: 100%");
  }
}
