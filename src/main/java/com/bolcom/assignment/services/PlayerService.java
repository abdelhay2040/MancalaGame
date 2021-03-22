package com.bolcom.assignment.services;

import com.bolcom.assignment.beans.PlayerBeans;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;

/**
 * PlayerService
 */
public interface PlayerService {

  /**
   * Get Player by Game and player's number.
   * 
   * @param game
   * @param playerNumber
   * @return
   */
  public Player getPlayerByGame(Game game, int playerNumber);

  /**
   * Create and save a new player.
   * 
   * @param playerBeans
   * @return
   */
  public Player createNewPlayer(String playerName, int playerNumber);

  /**
   * Convert Player Bean to Player object.
   * 
   * @param playerBeans
   * @return
   */
  public Player convertPlayerBeansToPlayer(PlayerBeans playerBeans);

  /**
   * Saves or updates player.
   * 
   * @param player
   * @return
   */
  public Player savePlayer(Player player);
}
