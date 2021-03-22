package com.bolcom.assignment.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import com.bolcom.assignment.beans.GameBeans;
import com.bolcom.assignment.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

/**
 * GameController
 */
@RestController
@RequestMapping("game")
public class GameController {

  @Autowired
  private IGameService gameService;
  private SimpMessagingTemplate simpMessagingTemplate;

  @GetMapping("/play/{gameId}")
  public ModelAndView fetchGamePage(@PathVariable("gameId") String gameId) {
    ModelAndView modelAndView = new ModelAndView("play");
    modelAndView.addObject("gameId", gameId);
    return modelAndView;
  }

  @PostMapping("/start/{playerOneName}")
  public Map<String, String> start(@PathVariable("playerOneName") String playerOneName) {
    String gameId = gameService.start(playerOneName).getId().toString();
    return Collections.singletonMap("gameId", gameId);
  }

  @PostMapping("/pick")
  public GameBeans pick(@RequestBody GameBeans gameBeans) {
 //   simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameBeans.getId(), gameBeans);
    return gameService.pick(gameBeans.getId(), gameBeans.getPlayerTurn(), gameBeans.getIndex());
  }

  @GetMapping("/load/{gameId}/{playerTwoName}")
  public Map<String, String> loadGame(@PathVariable("gameId") String gameId,@PathVariable("playerTwoName") String playerTwoName) {
    try {
      String loadedGameId = gameService.load(UUID.fromString(gameId),playerTwoName);
      return Collections.singletonMap("gameId", loadedGameId);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  @GetMapping("/{gameId}")
  public GameBeans getGame(@PathVariable("gameId") String gameId) {
    try {
      return gameService.getGameBeansById(UUID.fromString(gameId));
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

}
