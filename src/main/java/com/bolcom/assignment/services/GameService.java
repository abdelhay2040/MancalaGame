package com.bolcom.assignment.services;

import java.util.UUID;
import com.bolcom.assignment.beans.GameBeans;
import com.bolcom.assignment.models.Game;

/**
 * GameService
 */
public interface GameService {

  /**
   * First phase of the game<br>
   * <br>
   * Player pick a pit from their board and take all stones in that pit.
   * 
   * @param gameId
   * @param playerNumber
   * @param index
   */
  public GameBeans pick(UUID gameId, int playerNumber, int index);

  /**
   * Get an existing game by id and convert it to bean.
   * 
   * @param gameId
   * @return
   */
  public GameBeans getGameBeansById(UUID gameId);

  /**
   * Starts a new game.
   * 
   * @param gameBeans
   * @return
   */
  public GameBeans start(String playerOneName, String playerTwoname);

  /**
   * Loads existing game.<br>
   * Returns loaded game's id.
   * 
   * @param gameBeans
   * @return
   */
  public String load(UUID gameId);

  /**
   * Saves or updates a Game.
   * 
   * @param game
   * @return
   */
  public Game saveGame(Game game);

}
